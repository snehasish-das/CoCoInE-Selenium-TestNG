package org.cocoine.tests.ui.gorest;

import org.cocoine.core.BaseClass;
import org.cocoine.core.Config;
import org.cocoine.pages.ui.gorest.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SignInPageOpensTest extends BaseClass {
  @Test(groups = {"ui"}, description = "UI TC_05 - Sign in page opens")
  public void signInPageOpens() {
    HomePage page = new HomePage(getDriver()).open(Config.applicationUrl("gorest"));

    page.clickHref("/consumer/login");

    Assert.assertTrue(page.currentUrl().toLowerCase().contains("login") || page.hasText("GitHub") || page.hasText("Google"),
        "Sign in navigation should show authentication-related content.");
  }
}
