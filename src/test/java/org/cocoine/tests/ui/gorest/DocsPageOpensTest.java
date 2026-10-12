package org.cocoine.tests.ui.gorest;

import org.cocoine.core.BaseClass;
import org.cocoine.core.Config;
import org.cocoine.pages.ui.gorest.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DocsPageOpensTest extends BaseClass {
  @Test(groups = {"ui"}, description = "UI TC_04 - Docs page opens")
  public void docsPageOpens() {
    HomePage page = new HomePage(getDriver()).open(Config.applicationUrl("gorest"));

    page.clickHref("/docs");

    Assert.assertTrue(page.currentUrl().toLowerCase().contains("doc") || page.hasText("API"),
        "Docs navigation should show documentation-related content.");
  }
}
