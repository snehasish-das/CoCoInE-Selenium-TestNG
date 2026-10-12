package org.cocoine.tests.ui;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.Map;
import org.cocoine.framework.config.FrameworkConfig;
import org.cocoine.framework.data.JsonDataProvider;
import org.cocoine.framework.pages.sample.SamplePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SampleUiTest extends BaseUiTest {
  @Feature("UI")
  @Story("Sample page object flow")
  @Test(groups = {"smoke", "e2e"}, dataProvider = "sampleUiData", dataProviderClass = JsonDataProvider.class,
      enabled = false, description = "Disabled sample until an application URL and locators are provided")
  public void sampleSearchFlow(Map<String, Object> data) {
    String heading = new SamplePage(driver)
        .open(FrameworkConfig.appUrl("admin"))
        .search(String.valueOf(data.get("query")))
        .heading();

    Assert.assertFalse(heading.isBlank(), "Heading should be visible after search.");
  }
}
