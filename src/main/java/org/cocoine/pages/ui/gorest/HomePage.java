package org.cocoine.pages.ui.gorest;

import java.util.Map;
import org.cocoine.core.BaseClass;
import org.cocoine.core.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage extends BaseClass {
  private final WebDriver pageDriver;
  private final Map<String, String> locators;

  public HomePage(WebDriver driver) {
    this.pageDriver = driver;
    this.driver = driver;
    this.locators = getLocators("locators/gorest/HomePage.json");
  }

  public HomePage open(String url) {
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

  public boolean hasApiEndpointLink(String endpoint) {
    return isVisible(xpath(locators.get("apiEndpoint"), endpoint));
  }

  public void clickNavigation(String label) {
    clickFirstDisplayed(xpath(locators.get("navLink"), label));
  }

  public void clickAction(String value) {
    click(xpath(locators.get("actionLink"), value));
  }

  public void clickHref(String href) {
    clickFirstDisplayed(xpath(locators.get("href"), href));
  }

  public void openPath(String path) {
    pageDriver.get(Config.applicationUrl("gorest") + path);
  }

  private void clickFirstDisplayed(By locator) {
    for (WebElement element : pageDriver.findElements(locator)) {
      if (element.isDisplayed() && element.isEnabled()) {
        element.click();
        return;
      }
    }
    click(locator);
  }
}
