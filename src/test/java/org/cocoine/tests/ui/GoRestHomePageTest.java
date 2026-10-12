package org.cocoine.tests.ui;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.time.Duration;
import org.cocoine.framework.config.FrameworkConfig;
import org.cocoine.framework.pages.gorest.GoRestHomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GoRestHomePageTest extends BaseUiTest {
  private String appUrl() {
    return FrameworkConfig.appUrl("gorest");
  }

  @Feature("GoREST UI")
  @Story("Homepage navigation")
  @Test(groups = {"smoke", "BAT", "regression", "P1"},
      description = "UI TC_01 - Homepage Navigation And Tools Section Validation")
  public void homepageNavigationAndToolsSectionValidation() {
    GoRestHomePage page = new GoRestHomePage(driver).open(appUrl());

    Assert.assertTrue(page.isBrandVisible(), "GoREST brand or site name should be visible.");
    Assert.assertTrue(page.isNavigationVisible(), "Main navigation should be visible.");

    SoftAssert softly = new SoftAssert();
    softly.assertTrue(page.hasVisibleText("Docs"), "Docs navigation should be visible.");
    softly.assertTrue(page.hasVisibleText("Tools"), "Tools navigation should be visible.");
    softly.assertTrue(page.hasVisibleText("Learn"), "Learn navigation should be visible.");
    softly.assertTrue(page.hasVisibleText("Recipes"), "Recipes navigation should be visible.");
    softly.assertTrue(page.hasVisibleText("Q&A"), "Q&A navigation should be visible.");
    softly.assertTrue(page.hasVisibleText("Sign in"), "Sign in navigation should be visible.");
    softly.assertAll();

    page.clickNavigation("Tools");
    new WebDriverWait(driver, Duration.ofSeconds(FrameworkConfig.timeoutSeconds()))
        .until(ExpectedConditions.or(
            ExpectedConditions.urlContains("tools"),
            ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Tools"),
            ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "console")));

    Assert.assertTrue(driver.getCurrentUrl().contains("tools") || page.hasAnyVisibleText("Tools", "console"),
        "URL or visible content should confirm the Tools area.");
  }

  @Feature("GoREST UI")
  @Story("Authentication navigation")
  @Test(groups = {"smoke", "BAT", "regression", "P1"},
      description = "UI TC_02 - Sign In Navigation And Login Page Visibility Validation")
  public void signInNavigationAndLoginPageVisibilityValidation() {
    GoRestHomePage page = new GoRestHomePage(driver).open(appUrl());

    page.clickNavigation("Sign in");
    new WebDriverWait(driver, Duration.ofSeconds(FrameworkConfig.timeoutSeconds()))
        .until(ExpectedConditions.or(
            ExpectedConditions.urlContains("sign"),
            ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Sign in"),
            ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "GitHub"),
            ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Google")));

    Assert.assertTrue(driver.getCurrentUrl().contains("sign") || page.hasAnyVisibleText("Sign in", "GitHub", "Google"),
        "Sign in page should show authentication-related content.");
  }

  @Feature("GoREST UI")
  @Story("Homepage components")
  @Test(groups = {"regression", "P1"},
      description = "UI TC_04 - UI Component Validation For Navigation, Buttons, And Content Sections")
  public void uiComponentValidationForNavigationButtonsAndContentSections() {
    GoRestHomePage page = new GoRestHomePage(driver).open(appUrl());

    Assert.assertTrue(page.isNavigationVisible(), "Navigation bar should be visible.");
    Assert.assertTrue(page.endpointCardCount() >= 4, "API resource content should show users/posts/comments/todos.");
    Assert.assertTrue(page.actionButtonCount() > 0, "Expected clickable actions should be present.");

    WebElement docs = driver.findElement(By.xpath("//a[contains(normalize-space(), 'Docs')]"));
    Assert.assertTrue(docs.isDisplayed(), "Docs link should be visible.");
    Assert.assertTrue(docs.isEnabled(), "Docs link should be enabled.");
  }

  @Feature("GoREST UI")
  @Story("Locator strategy")
  @Test(groups = {"regression", "P2"},
      description = "UI TC_05 - Locator Strategy Validation Using TestId Concept And Fallback Locators")
  public void locatorStrategyValidationUsingTestIdConceptAndFallbackLocators() {
    GoRestHomePage page = new GoRestHomePage(driver).open(appUrl());

    if (page.testIdIsAvailable()) {
      Assert.assertFalse(driver.findElements(By.cssSelector("[data-testid]")).isEmpty(),
          "At least one data-testid locator should be available.");
    } else {
      Assert.assertTrue(driver.findElement(By.xpath("//a[contains(normalize-space(), 'Tools')]")).isDisplayed(),
          "Role/text-style fallback locator should identify the Tools navigation item.");
    }

    page.clickNavigation("Tools");
    Assert.assertTrue(page.hasAnyVisibleText("Tools", "console"),
        "Fallback locator interaction should navigate to the expected UI area.");
  }

  @Feature("GoREST UI")
  @Story("Assertions")
  @Test(groups = {"regression", "P2"},
      description = "UI TC_10 - Assertion-Focused Validation Using Selenium And TestNG Assertions")
  public void assertionFocusedValidationUsingSeleniumAndTestNgAssertions() {
    GoRestHomePage page = new GoRestHomePage(driver).open(appUrl());
    SoftAssert softly = new SoftAssert();

    softly.assertTrue(driver.getTitle().contains("Go REST") || driver.getTitle().contains("GoREST"),
        "Page title should contain GoREST.");
    softly.assertTrue(driver.getCurrentUrl().startsWith("https://gorest.co.in"),
        "Current URL should be the GoREST website.");
    softly.assertTrue(page.isBrandVisible(), "Main brand content should be visible.");
    softly.assertTrue(page.hasVisibleText("Users"), "Users resource should be visible.");
    softly.assertTrue(page.hasVisibleText("Posts"), "Posts resource should be visible.");
    softly.assertTrue(page.hasVisibleText("Comments"), "Comments resource should be visible.");
    softly.assertTrue(page.hasVisibleText("Todos"), "Todos resource should be visible.");
    softly.assertTrue(driver.findElements(By.xpath("//*[contains(normalize-space(), 'Internal Server Error')]"))
        .isEmpty(), "Server error text should not be visible.");
    softly.assertAll();
  }
}
