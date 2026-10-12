package org.cocoine.framework.execution;

import java.util.Map;
import java.util.Set;
import org.cocoine.framework.config.FrameworkConfig;

public final class TestExecutionPolicy {
  private static final Set<String> PROD_LIKE_ENVS = Set.of("prod", "production", "prod-like");
  private static final Set<String> PROD_APPROVED_SUITES = Set.of("prod-suite.xml");
  private static final Map<String, Integer> PROD_MAX_STEPS = Map.of(
      "gorest-api-crud", 6,
      "gorest-ui-navigation", 4);

  private TestExecutionPolicy() {
  }

  public static boolean isProdLike() {
    return PROD_LIKE_ENVS.contains(FrameworkConfig.environment().trim().toLowerCase());
  }

  public static int maxAllowedStep(String flowName) {
    if (!isProdLike()) {
      return Integer.MAX_VALUE;
    }
    return PROD_MAX_STEPS.getOrDefault(flowName, Integer.MAX_VALUE);
  }

  public static boolean canRunStep(String flowName, int stepNumber) {
    return stepNumber <= maxAllowedStep(flowName);
  }

  public static void requireProdApprovedSuite() {
    if (!isProdLike()) {
      return;
    }
    String selectedSuite = System.getProperty("suiteXmlFile", "");
    String selectedTest = System.getProperty("test", "");
    if (!selectedTest.isBlank()) {
      return;
    }
    boolean approved = PROD_APPROVED_SUITES.stream().anyMatch(selectedSuite::endsWith);
    if (!approved) {
      throw new IllegalStateException("Production-like environment '" + FrameworkConfig.environment()
          + "' may only run an approved production suite. Current suiteXmlFile='" + selectedSuite + "'.");
    }
  }
}
