package org.cocoine.tests.api.gorest;

import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.cocoine.pages.rest.gorest.ApiClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetPostCommentsTest {
  private final ApiClient api = new ApiClient();
  private int postId;

  @Test(groups = {"api"}, description = "API TC_10 - Pick a post id from comments")
  public void pickPostIdFromComments() {
    postId = api.getComments().jsonPath().getInt("[0].post_id");
    Assert.assertTrue(postId > 0, "Comment post id should be available.");
  }

  @Test(dependsOnMethods = "pickPostIdFromComments", groups = {"api"}, description = "API TC_10 - Get comments for post")
  public void getCommentsForPost() {
    Response response = api.getPostComments(postId);

    Assert.assertEquals(response.statusCode(), 200, "Post comments status should be 200.");
    List<Map<String, Object>> comments = response.jsonPath().getList("$");
    Assert.assertFalse(comments.isEmpty(), "Post comments should not be empty.");
    Assert.assertEquals(((Number) comments.get(0).get("post_id")).intValue(), postId, "Comment post id should match.");
  }
}
