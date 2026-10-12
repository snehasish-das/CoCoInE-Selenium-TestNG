package org.cocoine.tests.api;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import java.util.Map;
import org.cocoine.framework.api.RestClient;
import org.cocoine.framework.data.JsonDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SampleApiTest {
  @Feature("API")
  @Story("Reusable Rest Assured client")
  @Test(groups = {"smoke", "integration"}, dataProvider = "sampleApiData", dataProviderClass = JsonDataProvider.class,
      enabled = false, description = "Disabled sample until a reachable service URL is configured")
  public void getEndpointReturnsSuccess(Map<String, Object> data) {
    Response response = new RestClient().get(String.valueOf(data.get("path")));
    Assert.assertTrue(response.statusCode() < 500, "Service should not return a server error.");
  }
}
