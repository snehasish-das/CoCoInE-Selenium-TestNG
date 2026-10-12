package org.cocoine.core;

import com.fasterxml.jackson.databind.JsonNode;

public final class Config {
  private static final JsonNode ROOT = ResourceHelper.readJson("config/config.json");

  private Config() {
  }

  public static String value(String path) {
    return value(path, "");
  }

  public static String value(String path, String defaultValue) {
    String systemValue = System.getProperty(path);
    if (systemValue != null && !systemValue.isBlank()) {
      return systemValue;
    }

    JsonNode current = ROOT;
    for (String part : path.split("\\.")) {
      if (current == null || current.isMissingNode()) {
        return defaultValue;
      }
      current = current.path(part);
    }
    if (current == null || current.isMissingNode() || current.isNull()) {
      return defaultValue;
    }
    String text = current.asText();
    return text == null || text.isBlank() ? defaultValue : text;
  }

  public static String baseUrl() {
    return System.getProperty("baseUrl", value("Environment.baseUrl"));
  }

  public static String apiBaseUrl() {
    return System.getProperty("apiBaseUrl", value("Environment.apiBaseUrl"));
  }

  public static String applicationUrl(String appName) {
    return value("Applications." + appName);
  }

  public static String userValue(String userRole, String key) {
    return value("Users." + userRole + "." + key);
  }

  public static String browser() {
    return System.getProperty("browser", value("Environment.browser", "chrome"));
  }

  public static boolean remote() {
    return Boolean.parseBoolean(System.getProperty("remote", value("Environment.remote", "false")));
  }

  public static String gridUrl() {
    return System.getProperty("gridUrl", value("Environment.gridUrl", "http://localhost:4444/wd/hub"));
  }

  public static int timeoutSeconds() {
    return Integer.parseInt(System.getProperty("timeoutSeconds", value("Environment.timeoutSeconds", "20")));
  }
}
