package org.cocoine.tests.ui.gorest;

import org.cocoine.core.BaseClass;
import org.cocoine.core.Config;
import org.cocoine.pages.ui.gorest.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiResourcesVisibleTest extends BaseClass {
  @Test(groups = {"ui"}, description = "UI TC_03 - API resource content is visible")
  public void apiResourceContentIsVisible() {
    HomePage page = new HomePage(getDriver()).open(Config.applicationUrl("gorest"));

    Assert.assertTrue(page.hasApiResourceText(), "API resource text should be visible.");
    Assert.assertTrue(page.hasText("Users"), "Users resource text should be visible.");
    Assert.assertTrue(page.hasText("Posts"), "Posts resource text should be visible.");
  }
}
