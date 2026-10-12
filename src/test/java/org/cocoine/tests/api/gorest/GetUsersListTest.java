package org.cocoine.tests.api.gorest;

import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.cocoine.pages.rest.gorest.ApiClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetUsersListTest {
  private final ApiClient api = new ApiClient();

  @Test(groups = {"smoke", "api"}, description = "API TC_01 - Get users list")
  public void getUsersList() {
    Response response = api.getUsers();

    Assert.assertEquals(response.statusCode(), 200, "Users list status should be 200.");
    List<Map<String, Object>> users = response.jsonPath().getList("$");
    Assert.assertFalse(users.isEmpty(), "Users list should not be empty.");
    Assert.assertTrue(users.get(0).containsKey("id"), "User should contain id.");
    Assert.assertTrue(users.get(0).containsKey("name"), "User should contain name.");
    Assert.assertTrue(users.get(0).containsKey("email"), "User should contain email.");
  }
}
