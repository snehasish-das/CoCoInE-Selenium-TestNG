package org.cocoine.core.dryrun;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class DryRunReporter {
  private static final Pattern PACKAGE_PATTERN =
      Pattern.compile("(?m)^\\s*package\\s+([\\w.]+)\\s*;");
  private static final Pattern CLASS_PATTERN =
      Pattern.compile("\\b(?:public\\s+)?(?:final\\s+|abstract\\s+)?class\\s+(\\w+)");
  private static final Pattern TEST_METHOD_PATTERN =
      Pattern.compile(
          "@Test\\s*(?:\\((.*?)\\))?\\s*(?:public|protected|private)?\\s*(?:static\\s+)?[\\w<>\\[\\], ?]+\\s+(\\w+)\\s*\\(",
          Pattern.DOTALL);

  private DryRunReporter() {}

  public static void main(String[] args) throws Exception {
    Path projectDir = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
    Path reportPath =
        projectDir.resolve(System.getProperty("dryRun.reportFile", "target/dry-run-report.txt"));
    Files.createDirectories(reportPath.getParent());

    DryRunResult result = buildResult(projectDir);
    List<String> report = renderReport(result);

    Files.write(reportPath, report, StandardCharsets.UTF_8);
    report.forEach(System.out::println);
  }

  private static DryRunResult buildResult(Path projectDir) throws Exception {
    SuiteSelection suite = SuiteSelection.from(projectDir);
    List<TestMethod> discovered = discoverTests(projectDir.resolve("src/test/java"));
    List<TestMethod> participants =
        discovered.stream()
            .filter(suite::includes)
            .filter(DryRunReporter::matchesSystemGroups)
            .filter(DryRunReporter::matchesSurefireTestFilter)
            .collect(Collectors.toList());

    CompilationResult compilation = compileParticipants(projectDir, participants);
    return new DryRunResult(suite, discovered, participants, compilation);
  }

  private static List<TestMethod> discoverTests(Path testSourceRoot) throws IOException {
    if (!Files.isDirectory(testSourceRoot)) {
      return Collections.emptyList();
    }

    List<TestMethod> tests = new ArrayList<>();
    try (Stream<Path> paths = Files.walk(testSourceRoot)) {
      for (Path source : paths.filter(path -> path.toString().endsWith(".java")).collect(Collectors.toList())) {
        String content = Files.readString(source, StandardCharsets.UTF_8);
        String packageName = firstMatch(PACKAGE_PATTERN, content);
        String className = firstMatch(CLASS_PATTERN, content);
        if (packageName == null || className == null) {
          continue;
        }

        String qualifiedClassName = packageName + "." + className;
        Matcher matcher = TEST_METHOD_PATTERN.matcher(content);
        while (matcher.find()) {
          String attributes = matcher.group(1) == null ? "" : matcher.group(1);
          tests.add(
              new TestMethod(
                  source,
                  packageName,
                  qualifiedClassName,
                  className,
                  matcher.group(2),
                  valuesFor(attributes, "groups"),
                  valueFor(attributes, "description"),
                  valuesFor(attributes, "dependsOnMethods")));
        }
      }
    }

    tests.sort(TestMethod::compareTo);
    return tests;
  }

  private static CompilationResult compileParticipants(Path projectDir, List<TestMethod> participants)
      throws IOException {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null) {
      return CompilationResult.unavailable("No system Java compiler is available. Run dry run with a JDK.");
    }

    Set<Path> sources =
        participants.stream().map(TestMethod::source).collect(Collectors.toCollection(LinkedHashSet::new));
    if (sources.isEmpty()) {
      return CompilationResult.passed(Collections.emptyList());
    }

    Path outputDir = projectDir.resolve("target/dry-run-test-classes");
    Files.createDirectories(outputDir);

    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    StringWriter compilerOutput = new StringWriter();
    try (StandardJavaFileManager fileManager =
        compiler.getStandardFileManager(diagnostics, Locale.getDefault(), StandardCharsets.UTF_8)) {
      fileManager.setLocationFromPaths(StandardLocation.CLASS_OUTPUT, Collections.singleton(outputDir));

      List<String> options =
          Arrays.asList(
              "--release",
              System.getProperty("maven.compiler.release", "11"),
              "-classpath",
              classpath(projectDir));
      Iterable<? extends JavaFileObject> compilationUnits =
          fileManager.getJavaFileObjectsFromPaths(sources);
      boolean success =
          compiler
              .getTask(new PrintWriter(compilerOutput), fileManager, diagnostics, options, null, compilationUnits)
              .call();

      List<String> messages = diagnosticMessages(projectDir, diagnostics.getDiagnostics());
      if (!compilerOutput.toString().isBlank()) {
        messages.add(compilerOutput.toString().trim());
      }
      return success ? CompilationResult.passed(messages) : CompilationResult.failed(messages);
    }
  }

  private static String classpath(Path projectDir) {
    Set<String> entries = new LinkedHashSet<>();
    entries.add(projectDir.resolve("target/classes").toString());
    entries.add(projectDir.resolve("target/test-classes").toString());
    entries.addAll(readClasspathFile(projectDir.resolve("target/dry-run-classpath.txt")));
    entries.addAll(splitPath(System.getProperty("java.class.path", "")));
    entries.addAll(splitPath(System.getProperty("exec.classpath", "")));
    return String.join(System.getProperty("path.separator"), entries);
  }

  private static List<String> readClasspathFile(Path classpathFile) {
    if (!Files.isRegularFile(classpathFile)) {
      return Collections.emptyList();
    }

    try {
      return splitPath(Files.readString(classpathFile, StandardCharsets.UTF_8).trim());
    } catch (IOException exception) {
      return Collections.emptyList();
    }
  }

  private static List<String> splitPath(String classpath) {
    if (classpath.isBlank()) {
      return Collections.emptyList();
    }
    return Arrays.stream(classpath.split(Pattern.quote(System.getProperty("path.separator"))))
        .filter(entry -> !entry.isBlank())
        .collect(Collectors.toList());
  }

  private static List<String> diagnosticMessages(
      Path projectDir, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
    List<String> messages = new ArrayList<>();
    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
      JavaFileObject source = diagnostic.getSource();
      String location = source == null ? "<unknown>" : projectDir.relativize(Paths.get(source.toUri())).toString();
      messages.add(
          String.format(
              "%s:%d:%d: %s: %s",
              location,
              diagnostic.getLineNumber(),
              diagnostic.getColumnNumber(),
              diagnostic.getKind(),
              diagnostic.getMessage(Locale.getDefault())));
    }
    return messages;
  }

  private static boolean matchesSystemGroups(TestMethod test) {
    Set<String> included = csvProperty("groups");
    Set<String> excluded = csvProperty("excludedGroups");

    boolean includedMatch = included.isEmpty() || intersects(test.groups(), included);
    boolean excludedMatch = !excluded.isEmpty() && intersects(test.groups(), excluded);
    return includedMatch && !excludedMatch;
  }

  private static boolean matchesSurefireTestFilter(TestMethod test) {
    String filter = System.getProperty("test", "").trim();
    if (filter.isEmpty() || filter.startsWith("${")) {
      return true;
    }

    for (String candidate : filter.split(",")) {
      String trimmed = candidate.trim();
      if (trimmed.isEmpty()) {
        continue;
      }

      String[] parts = trimmed.split("#", 2);
      String classFilter = parts[0].replace(".java", "");
      String methodFilter = parts.length == 2 ? parts[1] : "";
      if (matchesClassFilter(test, classFilter) && matchesMethodFilter(test, methodFilter)) {
        return true;
      }
    }
    return false;
  }

  private static boolean matchesClassFilter(TestMethod test, String classFilter) {
    String regex = wildcardToRegex(classFilter);
    return test.simpleClassName().matches(regex) || test.qualifiedClassName().matches(regex);
  }

  private static boolean matchesMethodFilter(TestMethod test, String methodFilter) {
    return methodFilter.isBlank() || test.methodName().matches(wildcardToRegex(methodFilter));
  }

  private static String wildcardToRegex(String text) {
    StringBuilder regex = new StringBuilder();
    for (char ch : text.toCharArray()) {
      if (ch == '*') {
        regex.append(".*");
      } else if (ch == '?') {
        regex.append('.');
      } else {
        regex.append(Pattern.quote(String.valueOf(ch)));
      }
    }
    return regex.toString();
  }

  private static Set<String> csvProperty(String propertyName) {
    String value = System.getProperty(propertyName, "").trim();
    if (value.isEmpty() || value.startsWith("${")) {
      return Collections.emptySet();
    }
    return Arrays.stream(value.split(","))
        .map(String::trim)
        .filter(token -> !token.isEmpty())
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  private static boolean intersects(Collection<String> left, Collection<String> right) {
    for (String value : left) {
      if (right.contains(value)) {
        return true;
      }
    }
    return false;
  }

  private static String firstMatch(Pattern pattern, String content) {
    Matcher matcher = pattern.matcher(content);
    return matcher.find() ? matcher.group(1) : null;
  }

  private static List<String> valuesFor(String attributes, String key) {
    Matcher arrayMatcher =
        Pattern.compile(key + "\\s*=\\s*\\{([^}]*)}", Pattern.DOTALL).matcher(attributes);
    if (arrayMatcher.find()) {
      return quotedValues(arrayMatcher.group(1));
    }

    Matcher singleMatcher = Pattern.compile(key + "\\s*=\\s*\"([^\"]*)\"").matcher(attributes);
    if (singleMatcher.find()) {
      return Collections.singletonList(singleMatcher.group(1));
    }

    return Collections.emptyList();
  }

  private static String valueFor(String attributes, String key) {
    Matcher matcher = Pattern.compile(key + "\\s*=\\s*\"([^\"]*)\"").matcher(attributes);
    return matcher.find() ? matcher.group(1) : "";
  }

  private static List<String> quotedValues(String value) {
    Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(value);
    List<String> values = new ArrayList<>();
    while (matcher.find()) {
      values.add(matcher.group(1));
    }
    return values;
  }

  private static List<String> renderReport(DryRunResult result) {
    List<String> lines = new ArrayList<>();
    lines.add("");
    lines.add("========== CoCoInE Dry Run ==========");
    lines.add("Suite XML: " + result.suite().suiteXmlFile());
    lines.add("Suite packages: " + display(result.suite().packages()));
    lines.add("Suite include groups: " + display(result.suite().includedGroups()));
    lines.add("Suite exclude groups: " + display(result.suite().excludedGroups()));
    lines.add("System groups: " + display(csvProperty("groups")));
    lines.add("System excludedGroups: " + display(csvProperty("excludedGroups")));
    lines.add("Surefire -Dtest: " + blankAsAll(System.getProperty("test", "")));
    lines.add("");
    lines.add(
        String.format(
            "Discovered TestNG methods: %d | Participating methods: %d",
            result.discovered().size(), result.participants().size()));
    lines.add("Compilation: " + (result.compilation().success() ? "PASS" : "FAIL"));
    if (result.compilation().unavailableReason() != null) {
      lines.add("Compilation note: " + result.compilation().unavailableReason());
    }
    if (!result.compilation().messages().isEmpty()) {
      lines.add("");
      lines.add("Compilation details:");
      result.compilation().messages().forEach(message -> lines.add("  " + message));
    }
    lines.add("");
    lines.add("Participating tests:");
    if (result.participants().isEmpty()) {
      lines.add("  <none>");
    } else {
      for (TestMethod test : result.participants()) {
        lines.add(
            String.format(
                "  - %s#%s | groups=%s | description=\"%s\"%s",
                test.qualifiedClassName(),
                test.methodName(),
                display(test.groups()),
                test.description(),
                test.dependsOnMethods().isEmpty()
                    ? ""
                    : " | dependsOnMethods=" + display(test.dependsOnMethods())));
      }
    }
    lines.add("======================================");
    return lines;
  }

  private static String blankAsAll(String value) {
    return value == null || value.isBlank() ? "<all>" : value;
  }

  private static String display(Collection<String> values) {
    return values == null || values.isEmpty() ? "<all>" : String.join(",", values);
  }

  private static final class DryRunResult {
    private final SuiteSelection suite;
    private final List<TestMethod> discovered;
    private final List<TestMethod> participants;
    private final CompilationResult compilation;

    private DryRunResult(
        SuiteSelection suite,
        List<TestMethod> discovered,
        List<TestMethod> participants,
        CompilationResult compilation) {
      this.suite = suite;
      this.discovered = discovered;
      this.participants = participants;
      this.compilation = compilation;
    }

    private SuiteSelection suite() {
      return suite;
    }

    private List<TestMethod> discovered() {
      return discovered;
    }

    private List<TestMethod> participants() {
      return participants;
    }

    private CompilationResult compilation() {
      return compilation;
    }
  }

  private static final class CompilationResult {
    private final boolean success;
    private final List<String> messages;
    private final String unavailableReason;

    private CompilationResult(boolean success, List<String> messages, String unavailableReason) {
      this.success = success;
      this.messages = messages;
      this.unavailableReason = unavailableReason;
    }

    static CompilationResult passed(List<String> messages) {
      return new CompilationResult(true, messages, null);
    }

    static CompilationResult failed(List<String> messages) {
      return new CompilationResult(false, messages, null);
    }

    static CompilationResult unavailable(String reason) {
      return new CompilationResult(false, Collections.emptyList(), reason);
    }

    private boolean success() {
      return success;
    }

    private List<String> messages() {
      return messages;
    }

    private String unavailableReason() {
      return unavailableReason;
    }
  }

  private static final class TestMethod implements Comparable<TestMethod> {
    private final Path source;
    private final String packageName;
    private final String qualifiedClassName;
    private final String simpleClassName;
    private final String methodName;
    private final List<String> groups;
    private final String description;
    private final List<String> dependsOnMethods;

    private TestMethod(
        Path source,
        String packageName,
        String qualifiedClassName,
        String simpleClassName,
        String methodName,
        List<String> groups,
        String description,
        List<String> dependsOnMethods) {
      this.source = source;
      this.packageName = packageName;
      this.qualifiedClassName = qualifiedClassName;
      this.simpleClassName = simpleClassName;
      this.methodName = methodName;
      this.groups = groups;
      this.description = description;
      this.dependsOnMethods = dependsOnMethods;
    }

    private Path source() {
      return source;
    }

    private String packageName() {
      return packageName;
    }

    private String qualifiedClassName() {
      return qualifiedClassName;
    }

    private String simpleClassName() {
      return simpleClassName;
    }

    private String methodName() {
      return methodName;
    }

    private List<String> groups() {
      return groups;
    }

    private String description() {
      return description;
    }

    private List<String> dependsOnMethods() {
      return dependsOnMethods;
    }

    @Override
    public int compareTo(TestMethod other) {
      int classCompare = qualifiedClassName.compareTo(other.qualifiedClassName);
      return classCompare != 0 ? classCompare : methodName.compareTo(other.methodName);
    }
  }

  private static final class SuiteSelection {
    private final String suiteXmlFile;
    private final Set<String> packages;
    private final Set<String> includedGroups;
    private final Set<String> excludedGroups;

    private SuiteSelection(
        String suiteXmlFile,
        Set<String> packages,
        Set<String> includedGroups,
        Set<String> excludedGroups) {
      this.suiteXmlFile = suiteXmlFile;
      this.packages = packages;
      this.includedGroups = includedGroups;
      this.excludedGroups = excludedGroups;
    }

    static SuiteSelection from(Path projectDir) throws Exception {
      String suiteFile = System.getProperty("suiteXmlFile", "testng.xml");
      Path suitePath = projectDir.resolve(suiteFile).normalize();
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      disableExternalDtd(factory);
      Document document = factory.newDocumentBuilder().parse(suitePath.toFile());
      document.getDocumentElement().normalize();

      return new SuiteSelection(
          suiteFile,
          attributes(document, "package", "name"),
          groupNames(document, "include"),
          groupNames(document, "exclude"));
    }

    private static void disableExternalDtd(DocumentBuilderFactory factory)
        throws ParserConfigurationException {
      factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
      factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    }

    boolean includes(TestMethod test) {
      boolean packageMatch =
          packages.isEmpty()
              || packages.stream()
                  .anyMatch(packageName -> test.packageName().equals(packageName) || test.packageName().startsWith(packageName + "."));
      boolean includedGroupMatch = includedGroups.isEmpty() || intersects(test.groups(), includedGroups);
      boolean excludedGroupMatch = !excludedGroups.isEmpty() && intersects(test.groups(), excludedGroups);
      return packageMatch && includedGroupMatch && !excludedGroupMatch;
    }

    private String suiteXmlFile() {
      return suiteXmlFile;
    }

    private Set<String> packages() {
      return packages;
    }

    private Set<String> includedGroups() {
      return includedGroups;
    }

    private Set<String> excludedGroups() {
      return excludedGroups;
    }

    private static Set<String> attributes(Document document, String tagName, String attributeName) {
      NodeList nodes = document.getElementsByTagName(tagName);
      Set<String> values = new TreeSet<>();
      for (int index = 0; index < nodes.getLength(); index++) {
        Element element = (Element) nodes.item(index);
        String value = element.getAttribute(attributeName).trim();
        if (!value.isEmpty()) {
          values.add(value);
        }
      }
      return values;
    }

    private static Set<String> groupNames(Document document, String tagName) {
      NodeList nodes = document.getElementsByTagName(tagName);
      Set<String> values = new TreeSet<>();
      for (int index = 0; index < nodes.getLength(); index++) {
        Element element = (Element) nodes.item(index);
        if ("run".equals(element.getParentNode().getNodeName())) {
          String value = element.getAttribute("name").trim();
          if (!value.isEmpty()) {
            values.add(value);
          }
        }
      }
      return values;
    }
  }
}
