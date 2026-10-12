package org.cocoine.tests.ui.gorest;

import org.cocoine.core.BaseClass;
import org.cocoine.core.Config;
import org.cocoine.pages.ui.gorest.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NavigationVisibleTest extends BaseClass {
  @Test(groups = {"ui"}, description = "UI TC_02 - Navigation is visible")
  public void navigationIsVisible() {
    HomePage page = new HomePage(getDriver()).open(Config.applicationUrl("gorest"));

    Assert.assertTrue(page.navigationIsVisible(), "Header or navigation area should be visible.");
    Assert.assertTrue(page.hasText("Docs"), "Docs navigation text should be visible.");
    Assert.assertTrue(page.hasText("Tools"), "Tools navigation text should be visible.");
  }
}
