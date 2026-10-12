package org.cocoine.tests.api;

import org.cocoine.framework.config.FrameworkConfig;
import org.cocoine.framework.ui.LocatorRepository;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FrameworkWiringTest {
  @Test(groups = {"smoke", "sanity"}, description = "Verifies config and locator resources are loadable")
  public void frameworkResourcesLoad() {
    Assert.assertEquals(FrameworkConfig.browser(), "chrome");
    Assert.assertNotNull(new LocatorRepository("sample", "sample-page").by("dynamicLink", "Help"));
  }
}
