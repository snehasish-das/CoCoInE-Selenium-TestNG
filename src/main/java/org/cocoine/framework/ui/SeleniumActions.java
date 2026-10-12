package org.cocoine.framework.ui;

import java.time.Duration;
import java.util.ArrayList;
import org.cocoine.framework.config.FrameworkConfig;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumActions {
  protected final WebDriver driver;
  protected final WebDriverWait wait;

  public SeleniumActions(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConfig.timeoutSeconds()));
  }

  public void clickByXpath(String xpath) {
    waitForClickability(By.xpath(xpath)).click();
  }

  public void clickUsingJavaScript(By locator) {
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", waitForVisibility(locator));
  }

  public void clickUsingMouse(By locator) {
    new Actions(driver).moveToElement(waitForVisibility(locator)).click().perform();
  }

  public void setInputText(By locator, String text) {
    WebElement element = waitForVisibility(locator);
    element.clear();
    element.sendKeys(text);
  }

  public void selectDropdownValue(By locator, String visibleText) {
    new Select(waitForVisibility(locator)).selectByVisibleText(visibleText);
  }

  public String readText(By locator) {
    return waitForVisibility(locator).getText();
  }

  public String readValue(By locator) {
    return waitForVisibility(locator).getAttribute("value");
  }

  public WebElement waitForVisibility(By locator) {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  public WebElement waitForClickability(By locator) {
    return wait.until(ExpectedConditions.elementToBeClickable(locator));
  }

  public void switchFrame(By locator) {
    wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
  }

  public void switchToWindowOrTab(int index) {
    wait.until(driver -> driver.getWindowHandles().size() > index);
    driver.switchTo().window(new ArrayList<>(driver.getWindowHandles()).get(index));
  }

  public Alert alert() {
    return wait.until(ExpectedConditions.alertIsPresent());
  }

  public void openNewTab(String url) {
    ((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');", url);
    switchToWindowOrTab(driver.getWindowHandles().size() - 1);
  }
}
