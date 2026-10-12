package org.cocoine.tests.ui.gorest;

import org.cocoine.core.BaseClass;
import org.cocoine.core.Config;
import org.cocoine.pages.ui.gorest.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ToolsPageOpensTest extends BaseClass {
  @Test(groups = {"ui"}, description = "UI TC_06 - Tools page opens")
  public void toolsPageOpens() {
    HomePage page = new HomePage(getDriver()).open(Config.applicationUrl("gorest"));

    page.clickHref("/tools");

    Assert.assertTrue(page.currentUrl().toLowerCase().contains("tools") || page.hasText("Tools"),
        "Tools page should open.");
  }
}
