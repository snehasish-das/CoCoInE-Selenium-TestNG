package org.cocoine.tests.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import java.util.Iterator;
import org.cocoine.core.ResourceHelper;
import org.cocoine.pages.rest.SampleApi;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SampleApiTest {
  @DataProvider(name = "sampleApiData")
  public Iterator<Object[]> sampleApiData() {
    JsonNode records = ResourceHelper.readJson("testData/sample-api-data.json").path("records");
    java.util.List<Object[]> data = new java.util.ArrayList<>();
    records.forEach(record -> data.add(new Object[] {record}));
    return data.iterator();
  }

  @Test(groups = {"smoke", "api"}, dataProvider = "sampleApiData",
      description = "Sample API test using the generic REST wrapper")
  public void getEndpointReturnsSuccess(JsonNode data) {
    Response response = new SampleApi().getResource(data.path("path").asText());

    Assert.assertTrue(response.statusCode() >= 200 && response.statusCode() < 500,
        "Sample API should return a non-server-error response.");
  }
}
