package org.cocoine.tests.ui.gorest;

import org.cocoine.core.BaseClass;
import org.cocoine.core.Config;
import org.cocoine.pages.ui.gorest.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomePageLoadsTest extends BaseClass {
  @Test(groups = {"smoke", "ui"}, description = "UI TC_01 - Home page loads")
  public void homePageLoads() {
    HomePage page = new HomePage(getDriver()).open(Config.applicationUrl("gorest"));

    Assert.assertTrue(page.currentUrl().contains("gorest.co.in"), "Current URL should be GoREST.");
    Assert.assertFalse(page.title().isBlank(), "Page title should not be blank.");
    Assert.assertTrue(page.brandIsVisible(), "GoREST brand text should be visible.");
  }
}
