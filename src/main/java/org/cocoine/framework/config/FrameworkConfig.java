package org.cocoine.framework.config;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;
import org.cocoine.framework.util.JsonLoader;

public final class FrameworkConfig {
  private static final JsonNode ROOT = JsonLoader.readTree("config/config.json");
  private static final String ENV = System.getProperty("env", "local");

  private FrameworkConfig() {
  }

  public static String environment() {
    return ENV;
  }

  public static String browser() {
    return System.getProperty("browser", value("defaults.browser", "chrome"));
  }

  public static boolean remote() {
    return Boolean.parseBoolean(System.getProperty("remote", value("defaults.remote", "false")));
  }

  public static String gridUrl() {
    return System.getProperty("gridUrl", value("defaults.gridUrl", "http://localhost:4444/wd/hub"));
  }

  public static int timeoutSeconds() {
    return Integer.parseInt(System.getProperty("timeoutSeconds", value("defaults.timeoutSeconds", "20")));
  }

  public static String appUrl(String key) {
    return value("environments." + ENV + ".apps." + key, "");
  }

  public static String restBaseUrl() {
    return System.getProperty("restBaseUrl", value("environments." + ENV + ".restBaseUrl", ""));
  }

  public static String user(String profile, String field) {
    return value("users." + profile + "." + field, "");
  }

  public static String value(String dottedPath, String defaultValue) {
    JsonNode node = ROOT;
    for (String part : dottedPath.split("\\.")) {
      if (node == null || node.isMissingNode()) {
        return defaultValue;
      }
      node = node.path(part);
    }
    if (node == null || node.isMissingNode() || node.isNull()) {
      return defaultValue;
    }
    return Optional.ofNullable(node.asText()).filter(text -> !text.isBlank()).orElse(defaultValue);
  }
}
