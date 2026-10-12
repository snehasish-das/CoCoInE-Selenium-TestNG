package org.cocoine.tests.api.gorest;

import io.restassured.response.Response;
import org.cocoine.pages.rest.gorest.ApiClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetSingleUserTest {
  private final ApiClient api = new ApiClient();
  private int userId;

  @Test(groups = {"api"}, description = "API TC_02 - Pick a user id from the list")
  public void pickUserId() {
    userId = api.getUsers().jsonPath().getInt("[0].id");
    Assert.assertTrue(userId > 0, "User id should be available.");
  }

  @Test(dependsOnMethods = "pickUserId", groups = {"api"}, description = "API TC_02 - Get single user")
  public void getSingleUser() {
    Response response = api.getUser(userId);

    Assert.assertEquals(response.statusCode(), 200, "Single user status should be 200.");
    Assert.assertEquals(response.jsonPath().getInt("id"), userId, "Returned user id should match.");
    Assert.assertNotNull(response.jsonPath().getString("name"), "User name should be present.");
  }
}
