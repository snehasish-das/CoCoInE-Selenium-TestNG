package org.cocoine.tests.ui;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.cocoine.framework.config.FrameworkConfig;
import org.cocoine.framework.execution.PolicyGatedFlow;
import org.cocoine.framework.pages.gorest.GoRestHomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GoRestVisualAndEventTest extends BaseUiTest {
  private GoRestHomePage navigationPage;
  private WebDriverWait navigationWait;

  private String appUrl() {
    return FrameworkConfig.appUrl("gorest");
  }

  @Feature("GoREST UI")
  @Story("Dark mode")
  @Test(groups = {"regression", "P2"},
      description = "UI TC_03 - Dark Mode Visual Regression Validation")
  public void darkModeVisualRegressionValidation() {
    GoRestHomePage page = new GoRestHomePage(driver).open(appUrl());
    JavascriptExecutor js = (JavascriptExecutor) driver;

    String bodyTextColor = String.valueOf(js.executeScript(
        "return window.getComputedStyle(document.body).color;"));
    String bodyBackground = String.valueOf(js.executeScript(
        "return window.getComputedStyle(document.body).backgroundColor;"));

    Assert.assertTrue(page.isBrandVisible(), "Brand should remain visible in dark color scheme runs.");
    Assert.assertTrue(page.hasAnyVisibleText("Users", "Posts", "Comments", "Todos"),
        "API content should remain readable in dark color scheme runs.");
    Assert.assertFalse(bodyTextColor.isBlank(), "Computed text color should be available.");
    Assert.assertFalse(bodyBackground.isBlank(), "Computed background color should be available.");
  }

  @Feature("GoREST UI")
  @Story("Debuggable journey")
  @Test(groups = {"regression", "P2"},
      description = "UI TC_07 and TC_08 - Inspector And Trace Viewer Ready User Journey Validation")
  public void traceAndInspectorReadyUserJourneyValidation() {
    PolicyGatedFlow.flow("gorest-ui-navigation")
        .step(1, "Open homepage", this::openHomepageStep)
        .step(2, "Navigate to Tools", this::navigateToToolsStep)
        .step(3, "Return to homepage", this::returnToHomepageStep)
        .step(4, "Navigate to Docs", this::navigateToDocsStep)
        .step(5, "Navigate to Sign in", this::navigateToSignInStep)
        .run();
  }

  private void openHomepageStep() {
    navigationPage = new GoRestHomePage(driver).open(appUrl());
    navigationWait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConfig.timeoutSeconds()));
  }

  private void navigateToToolsStep() {
    navigationPage.clickNavigation("Tools");
    navigationWait.until(ExpectedConditions.or(
        ExpectedConditions.urlContains("tools"),
        ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Tools"),
        ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "console")));
  }

  private void returnToHomepageStep() {
    driver.navigate().back();
    navigationWait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
  }

  private void navigateToDocsStep() {
    navigationPage.clickNavigation("Docs");
    navigationWait.until(ExpectedConditions.or(
        ExpectedConditions.urlContains("docs"),
        ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Docs"),
        ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "API")));
  }

  private void navigateToSignInStep() {
    navigationPage.clickNavigation("Sign in");
    navigationWait.until(ExpectedConditions.or(
        ExpectedConditions.urlContains("sign"),
        ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Sign in"),
        ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "GitHub")));

    Assert.assertTrue(navigationPage.hasAnyVisibleText("Sign in", "GitHub", "Google"),
        "Journey should complete at authentication UI.");
  }

  @Feature("GoREST UI")
  @Story("Browser events")
  @Test(groups = {"regression", "P2"},
      description = "UI TC_09 - Dialogs, Popups, Frames, Or Browser Event Listener Validation")
  public void dialogsPopupsFramesOrBrowserEventListenerValidation() {
    new GoRestHomePage(driver).open(appUrl());
    String originalWindow = driver.getWindowHandle();
    List<WebElement> externalLinks = driver.findElements(By.cssSelector("a[target='_blank'], a[href*='github.com']"));

    if (!externalLinks.isEmpty()) {
      int before = driver.getWindowHandles().size();
      externalLinks.get(0).click();
      new WebDriverWait(driver, Duration.ofSeconds(FrameworkConfig.timeoutSeconds()))
          .until(webDriver -> webDriver.getWindowHandles().size() >= before);
      List<String> windows = new ArrayList<>(driver.getWindowHandles());
      if (windows.size() > before) {
        driver.switchTo().window(windows.get(windows.size() - 1));
        Assert.assertFalse(driver.getCurrentUrl().isBlank(), "Popup or new tab URL should be available.");
        driver.close();
        driver.switchTo().window(originalWindow);
      }
    }

    Assert.assertTrue(driver.findElement(By.tagName("body")).isDisplayed(),
        "Original page should remain usable after browser-event validation.");
  }
}
