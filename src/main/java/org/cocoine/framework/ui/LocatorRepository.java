package org.cocoine.framework.ui;

import com.fasterxml.jackson.databind.JsonNode;
import org.cocoine.framework.util.JsonLoader;
import org.openqa.selenium.By;

public class LocatorRepository {
  private final JsonNode locators;

  public LocatorRepository(String module, String page) {
    this.locators = JsonLoader.readTree("locators/" + module + "/" + page + ".json");
  }

  public By by(String key, String... params) {
    JsonNode node = locators.path(key);
    if (node.isMissingNode()) {
      throw new IllegalArgumentException("Locator key not found: " + key);
    }
    String type = node.path("type").asText("xpath");
    String value = replaceParams(node.path("value").asText(), params);
    switch (type.toLowerCase()) {
      case "id":
        return By.id(value);
      case "css":
      case "cssselector":
        return By.cssSelector(value);
      case "name":
        return By.name(value);
      case "xpath":
      default:
        return By.xpath(value);
    }
  }

  private String replaceParams(String value, String... params) {
    String resolved = value;
    for (String param : params) {
      resolved = resolved.replaceFirst("<<param>>", param);
    }
    return resolved;
  }
}
