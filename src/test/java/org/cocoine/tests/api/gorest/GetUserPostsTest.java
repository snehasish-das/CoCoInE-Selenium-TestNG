package org.cocoine.tests.api.gorest;

import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.cocoine.pages.rest.gorest.ApiClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetUserPostsTest {
  private final ApiClient api = new ApiClient();
  private int userId;

  @Test(groups = {"api"}, description = "API TC_09 - Pick a user id from posts")
  public void pickUserIdFromPosts() {
    userId = api.getPosts().jsonPath().getInt("[0].user_id");
    Assert.assertTrue(userId > 0, "Post user id should be available.");
  }

  @Test(dependsOnMethods = "pickUserIdFromPosts", groups = {"api"}, description = "API TC_09 - Get posts for user")
  public void getPostsForUser() {
    Response response = api.getUserPosts(userId);

    Assert.assertEquals(response.statusCode(), 200, "User posts status should be 200.");
    List<Map<String, Object>> posts = response.jsonPath().getList("$");
    Assert.assertFalse(posts.isEmpty(), "User posts should not be empty.");
    Assert.assertEquals(((Number) posts.get(0).get("user_id")).intValue(), userId, "Post user id should match.");
  }
}
