package org.cocoine.pages.ui;

import java.util.Map;
import org.cocoine.core.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SamplePage extends BaseClass {
  private final WebDriver pageDriver;
  private final Map<String, String> locators;

  public SamplePage(WebDriver driver) {
    this.pageDriver = driver;
    this.driver = driver;
    this.locators = getLocators("locators/sample/SamplePage.json");
  }

  public SamplePage open(String url) {
    pageDriver.get(url);
    return this;
  }

  public String title() {
    return pageDriver.getTitle();
  }

  public boolean bodyIsVisible() {
    return isVisible(By.xpath(locators.get("body")));
  }
}
