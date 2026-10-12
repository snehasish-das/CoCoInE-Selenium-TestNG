package org.cocoine.tests.api;

import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.cocoine.pages.rest.gorest.ApiClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GoRestApiTest {
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

  @Test(groups = {"api"}, description = "API TC_02 - Get a single user from list")
  public void getSingleUserFromList() {
    int userId = api.getUsers().jsonPath().getInt("[0].id");
    Response response = api.getUser(userId);

    Assert.assertEquals(response.statusCode(), 200, "Single user status should be 200.");
    Assert.assertEquals(response.jsonPath().getInt("id"), userId, "Returned user id should match requested id.");
    Assert.assertNotNull(response.jsonPath().getString("name"), "User name should be present.");
    Assert.assertNotNull(response.jsonPath().getString("email"), "User email should be present.");
  }

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

  @Test(groups = {"api"}, description = "API TC_04 - Get comments list")
  public void getCommentsList() {
    Response response = api.getComments();

    Assert.assertEquals(response.statusCode(), 200, "Comments list status should be 200.");
    List<Map<String, Object>> comments = response.jsonPath().getList("$");
    Assert.assertFalse(comments.isEmpty(), "Comments list should not be empty.");
    Assert.assertTrue(comments.get(0).containsKey("id"), "Comment should contain id.");
    Assert.assertTrue(comments.get(0).containsKey("post_id"), "Comment should contain post_id.");
    Assert.assertTrue(comments.get(0).containsKey("body"), "Comment should contain body.");
  }

  @Test(groups = {"api"}, description = "API TC_05 - Get todos list")
  public void getTodosList() {
    Response response = api.getTodos();

    Assert.assertEquals(response.statusCode(), 200, "Todos list status should be 200.");
    List<Map<String, Object>> todos = response.jsonPath().getList("$");
    Assert.assertFalse(todos.isEmpty(), "Todos list should not be empty.");
    Assert.assertTrue(todos.get(0).containsKey("id"), "Todo should contain id.");
    Assert.assertTrue(todos.get(0).containsKey("user_id"), "Todo should contain user_id.");
    Assert.assertTrue(todos.get(0).containsKey("status"), "Todo should contain status.");
  }
}
