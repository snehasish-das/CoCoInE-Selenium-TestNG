package org.cocoine.tests.ui.gorest;

import org.cocoine.core.BaseClass;
import org.cocoine.core.Config;
import org.cocoine.pages.ui.gorest.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RestConsolePageOpensTest extends BaseClass {
  @Test(groups = {"ui"}, description = "UI TC_07 - REST console page opens")
  public void restConsolePageOpens() {
    HomePage page = new HomePage(getDriver()).open(Config.applicationUrl("gorest"));

    page.clickHref("/rest-console");

    Assert.assertTrue(page.currentUrl().toLowerCase().contains("rest-console") || page.hasText("console"),
        "REST console page should open.");
  }
}
