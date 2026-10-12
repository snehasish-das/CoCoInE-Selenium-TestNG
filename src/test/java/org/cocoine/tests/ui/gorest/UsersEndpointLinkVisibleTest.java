package org.cocoine.tests.ui.gorest;

import org.cocoine.core.BaseClass;
import org.cocoine.core.Config;
import org.cocoine.pages.ui.gorest.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UsersEndpointLinkVisibleTest extends BaseClass {
  @Test(groups = {"ui"}, description = "UI TC_08 - Users endpoint link is visible")
  public void usersEndpointLinkIsVisible() {
    HomePage page = new HomePage(getDriver()).open(Config.applicationUrl("gorest"));

    Assert.assertTrue(page.hasApiEndpointLink("/public/v2/users"), "Users endpoint link should be visible.");
  }
}
