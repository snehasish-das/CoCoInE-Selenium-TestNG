package org.cocoine.tests.api;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.cocoine.framework.api.RestClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GoRestApiReadTest {
  private final RestClient client = new RestClient();

  @Feature("GoREST API")
  @Story("Users list")
  @Test(groups = {"smoke", "BAT", "regression", "P1"}, description = "TC_01 - Get Users List")
  public void getUsersListReturnsUserArray() {
    Response response = client.get("/users");

    Assert.assertEquals(response.statusCode(), 200, "Users list status code should be 200.");
    List<Map<String, Object>> users = response.jsonPath().getList("$");
    Assert.assertFalse(users.isEmpty(), "Users list should not be empty.");

    Map<String, Object> firstUser = users.get(0);
    Assert.assertTrue(firstUser.containsKey("id"), "User should contain id.");
    Assert.assertTrue(firstUser.containsKey("name"), "User should contain name.");
    Assert.assertTrue(firstUser.containsKey("email"), "User should contain email.");
    Assert.assertTrue(firstUser.containsKey("gender"), "User should contain gender.");
    Assert.assertTrue(firstUser.containsKey("status"), "User should contain status.");
  }
}
