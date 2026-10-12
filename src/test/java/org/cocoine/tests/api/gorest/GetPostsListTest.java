package org.cocoine.tests.api.gorest;

import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.cocoine.pages.rest.gorest.ApiClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetPostsListTest {
  private final ApiClient api = new ApiClient();

  @Test(groups = {"api"}, description = "API TC_03 - Get posts list")
  public void getPostsList() {
    Response response = api.getPosts();

    Assert.assertEquals(response.statusCode(), 200, "Posts list status should be 200.");
    List<Map<String, Object>> posts = response.jsonPath().getList("$");
    Assert.assertFalse(posts.isEmpty(), "Posts list should not be empty.");
    Assert.assertTrue(posts.get(0).containsKey("id"), "Post should contain id.");
    Assert.assertTrue(posts.get(0).containsKey("user_id"), "Post should contain user_id.");
    Assert.assertTrue(posts.get(0).containsKey("title"), "Post should contain title.");
  }
}
