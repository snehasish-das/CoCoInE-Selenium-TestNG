package org.cocoine.tests.api.gorest;

import io.restassured.response.Response;
import org.cocoine.pages.rest.gorest.ApiClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetSingleCommentTest {
  private final ApiClient api = new ApiClient();
  private int commentId;

  @Test(groups = {"api"}, description = "API TC_06 - Pick a comment id from the list")
  public void pickCommentId() {
    commentId = api.getComments().jsonPath().getInt("[0].id");
    Assert.assertTrue(commentId > 0, "Comment id should be available.");
  }

  @Test(dependsOnMethods = "pickCommentId", groups = {"api"}, description = "API TC_06 - Get single comment")
  public void getSingleComment() {
    Response response = api.getComment(commentId);

    Assert.assertEquals(response.statusCode(), 200, "Single comment status should be 200.");
    Assert.assertEquals(response.jsonPath().getInt("id"), commentId, "Returned comment id should match.");
    Assert.assertNotNull(response.jsonPath().getString("body"), "Comment body should be present.");
  }
}
