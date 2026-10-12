package org.cocoine.tests.ui;

import org.cocoine.core.BaseClass;
import org.cocoine.core.Config;
import org.cocoine.pages.ui.gorest.GoRestHomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GoRestUiTest extends BaseClass {
  private String appUrl() {
    return Config.applicationUrl("gorest");
  }

  @Test(groups = {"smoke", "ui"}, description = "UI TC_01 - Home page loads")
  public void homePageLoads() {
    GoRestHomePage page = new GoRestHomePage(getDriver()).open(appUrl());

    Assert.assertTrue(page.currentUrl().contains("gorest.co.in"), "Current URL should be GoREST.");
    Assert.assertFalse(page.title().isBlank(), "Page title should not be blank.");
    Assert.assertTrue(page.brandIsVisible(), "GoREST brand text should be visible.");
  }

  @Test(groups = {"ui"}, description = "UI TC_02 - Navigation is visible")
  public void navigationIsVisible() {
    GoRestHomePage page = new GoRestHomePage(getDriver()).open(appUrl());

    Assert.assertTrue(page.navigationIsVisible(), "Header or navigation area should be visible.");
    Assert.assertTrue(page.hasText("Docs"), "Docs navigation text should be visible.");
    Assert.assertTrue(page.hasText("Tools"), "Tools navigation text should be visible.");
  }

  @Test(groups = {"ui"}, description = "UI TC_03 - API resource content is visible")
  public void apiResourceContentIsVisible() {
    GoRestHomePage page = new GoRestHomePage(getDriver()).open(appUrl());

    Assert.assertTrue(page.hasApiResourceText(), "API resource text should be visible.");
    Assert.assertTrue(page.hasText("Users"), "Users resource text should be visible.");
    Assert.assertTrue(page.hasText("Posts"), "Posts resource text should be visible.");
  }

  @Test(groups = {"ui"}, description = "UI TC_04 - Docs navigation opens documentation")
  public void docsNavigationOpensDocumentation() {
    GoRestHomePage page = new GoRestHomePage(getDriver()).open(appUrl());

    page.clickNavigation("Docs");

    Assert.assertTrue(page.currentUrl().toLowerCase().contains("doc") || page.hasText("API"),
        "Docs navigation should show documentation-related content.");
  }

  @Test(groups = {"ui"}, description = "UI TC_05 - Sign in navigation opens authentication page")
  public void signInNavigationOpensAuthenticationPage() {
    GoRestHomePage page = new GoRestHomePage(getDriver()).open(appUrl());

    page.clickNavigation("Sign in");

    Assert.assertTrue(page.currentUrl().toLowerCase().contains("sign") || page.hasText("GitHub") || page.hasText("Google"),
        "Sign in navigation should show authentication-related content.");
  }
}
