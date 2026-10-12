package org.cocoine.tests.ui.gorest;

import org.cocoine.core.BaseClass;
import org.cocoine.core.Config;
import org.cocoine.pages.ui.gorest.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StatusPageOpensTest extends BaseClass {
  @Test(groups = {"ui"}, description = "UI TC_10 - Status page opens")
  public void statusPageOpens() {
    HomePage page = new HomePage(getDriver()).open(Config.applicationUrl("gorest"));

    page.clickHref("/status");

    Assert.assertTrue(page.currentUrl().toLowerCase().contains("status") || page.hasText("Status"),
        "Status page should open.");
  }
}
