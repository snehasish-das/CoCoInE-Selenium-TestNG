package org.cocoine.framework.pages.gorest;

import org.cocoine.framework.ui.LocatorRepository;
import org.cocoine.framework.ui.SeleniumActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GoRestHomePage extends SeleniumActions {
  private final LocatorRepository locators = new LocatorRepository("gorest", "home-page");

  public GoRestHomePage(WebDriver driver) {
    super(driver);
  }

  public GoRestHomePage open(String url) {
    driver.get(url);
    return this;
  }

  public boolean isBrandVisible() {
    return !driver.findElements(locators.by("brand")).isEmpty();
  }

  public boolean isNavigationVisible() {
    return !driver.findElements(locators.by("navigation")).isEmpty();
  }

  public void clickNavigation(String label) {
    waitForClickability(locators.by("navLink", label)).click();
  }

  public boolean hasVisibleText(String text) {
    return !driver.findElements(By.xpath("//*[contains(normalize-space(), '" + text + "')]")).isEmpty();
  }

  public boolean hasAnyVisibleText(String... texts) {
    for (String text : texts) {
      if (hasVisibleText(text)) {
        return true;
      }
    }
    return false;
  }

  public int endpointCardCount() {
    return driver.findElements(locators.by("endpointCards")).size();
  }

  public int actionButtonCount() {
    return driver.findElements(locators.by("actionButtons")).size();
  }

  public boolean testIdIsAvailable() {
    return !driver.findElements(By.cssSelector("[data-testid]")).isEmpty();
  }
}
