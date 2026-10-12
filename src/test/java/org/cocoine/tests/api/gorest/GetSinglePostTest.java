package org.cocoine.tests.api.gorest;

import io.restassured.response.Response;
import org.cocoine.pages.rest.gorest.ApiClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetSinglePostTest {
  private final ApiClient api = new ApiClient();
  private int postId;

  @Test(groups = {"api"}, description = "API TC_04 - Pick a post id from the list")
  public void pickPostId() {
    postId = api.getPosts().jsonPath().getInt("[0].id");
    Assert.assertTrue(postId > 0, "Post id should be available.");
  }

  @Test(dependsOnMethods = "pickPostId", groups = {"api"}, description = "API TC_04 - Get single post")
  public void getSinglePost() {
    Response response = api.getPost(postId);

    Assert.assertEquals(response.statusCode(), 200, "Single post status should be 200.");
    Assert.assertEquals(response.jsonPath().getInt("id"), postId, "Returned post id should match.");
    Assert.assertNotNull(response.jsonPath().getString("title"), "Post title should be present.");
  }
}
