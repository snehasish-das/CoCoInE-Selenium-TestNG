package org.cocoine.tests.ui;

import org.cocoine.core.BaseClass;
import org.cocoine.core.Config;
import org.cocoine.pages.ui.SamplePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SampleUiTest extends BaseClass {
  @Test(groups = {"smoke", "ui"}, description = "Sample UI test using a generic page object")
  public void homePageLoads() {
    SamplePage page = new SamplePage(getDriver()).open(Config.baseUrl());

    Assert.assertTrue(page.bodyIsVisible(), "Page body should be visible.");
    Assert.assertFalse(page.title().isBlank(), "Page title should not be blank.");
  }
}
