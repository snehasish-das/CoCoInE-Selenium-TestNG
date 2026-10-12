package org.cocoine.tests.api.gorest;

import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.cocoine.pages.rest.gorest.ApiClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetCommentsListTest {
  private final ApiClient api = new ApiClient();

  @Test(groups = {"api"}, description = "API TC_05 - Get comments list")
  public void getCommentsList() {
    Response response = api.getComments();

    Assert.assertEquals(response.statusCode(), 200, "Comments list status should be 200.");
    List<Map<String, Object>> comments = response.jsonPath().getList("$");
    Assert.assertFalse(comments.isEmpty(), "Comments list should not be empty.");
    Assert.assertTrue(comments.get(0).containsKey("id"), "Comment should contain id.");
    Assert.assertTrue(comments.get(0).containsKey("post_id"), "Comment should contain post_id.");
    Assert.assertTrue(comments.get(0).containsKey("body"), "Comment should contain body.");
  }
}
