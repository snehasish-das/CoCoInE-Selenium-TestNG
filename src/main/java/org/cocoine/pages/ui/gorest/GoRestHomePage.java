package org.cocoine.pages.ui.gorest;

import java.util.Map;
import org.cocoine.core.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GoRestHomePage extends BaseClass {
  private final WebDriver pageDriver;
  private final Map<String, String> locators;

  public GoRestHomePage(WebDriver driver) {
    this.pageDriver = driver;
    this.driver = driver;
    this.locators = getLocators("locators/gorest/HomePage.json");
  }

  public GoRestHomePage open(String url) {
    pageDriver.get(url);
    return this;
  }

  public String title() {
    return pageDriver.getTitle();
  }

  public String currentUrl() {
    return pageDriver.getCurrentUrl();
  }

  public boolean brandIsVisible() {
    return isVisible(By.xpath(locators.get("brand")));
  }

  public boolean navigationIsVisible() {
    return isVisible(By.xpath(locators.get("navigation")));
  }

  public boolean hasText(String text) {
    return isVisible(xpath(locators.get("bodyText"), text));
  }

  public boolean hasApiResourceText() {
    return isVisible(By.xpath(locators.get("resourceText")));
  }

  public void clickNavigation(String label) {
    click(xpath(locators.get("navLink"), label));
  }

  public void clickAction(String value) {
    click(xpath(locators.get("actionLink"), value));
  }
}
