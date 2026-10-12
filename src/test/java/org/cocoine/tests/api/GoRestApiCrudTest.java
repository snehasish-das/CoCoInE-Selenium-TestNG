package org.cocoine.tests.api;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import org.cocoine.framework.api.AuthSupport;
import org.cocoine.framework.api.RestClient;
import org.cocoine.framework.execution.PolicyGatedFlow;
import org.cocoine.framework.util.RandomData;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GoRestApiCrudTest {
  private final RestClient client = new RestClient();
  private String token;
  private int userId;
  private int postId;
  private int commentId;
  private int todoId;
  private String createdEmail;
  private String replacedEmail;

  @BeforeClass(alwaysRun = true)
  public void requireToken() {
    token = AuthSupport.gorestToken();
    if (token == null || token.isBlank()) {
      throw new SkipException("GOREST_TOKEN is required for GoREST write API tests.");
    }
    createdEmail = "cocoine." + RandomData.alphaNumeric(10).toLowerCase() + "@example.test";
    replacedEmail = "cocoine.updated." + RandomData.alphaNumeric(10).toLowerCase() + "@example.test";
  }

  @Feature("GoREST API")
  @Story("User CRUD")
  @Test(groups = {"regression", "P1"}, description = "TC_02 through TC_11 - Policy-gated API CRUD flow")
  public void gorestApiCrudPolicyGatedFlow() {
    PolicyGatedFlow.flow("gorest-api-crud")
        .step(1, "Create user", this::createUserStep)
        .step(2, "Replace user", this::replaceUserStep)
        .step(3, "Create post for user", this::createPostForUserStep)
        .step(4, "Get user posts", this::getUserPostsStep)
        .step(5, "Get single post", this::getSinglePostStep)
        .step(6, "Update post", this::updatePostStep)
        .step(7, "Create post comment", this::createPostCommentStep)
        .step(8, "Create todo", this::createTodoStep)
        .step(9, "Delete post", this::deletePostStep)
        .step(10, "Delete user", this::deleteUserStep)
        .run();
  }

  private void createUserStep() {
    Response response = client.authenticatedPost("/users", Map.of(
        "name", "Snehasish Das",
        "email", createdEmail,
        "gender", "male",
        "status", "active"), token);

    Assert.assertEquals(response.statusCode(), 201, "Create user status code should be 201.");
    userId = response.jsonPath().getInt("id");
    Assert.assertTrue(userId > 0, "Created user id should be present.");
    Assert.assertEquals(response.jsonPath().getString("name"), "Snehasish Das");
    Assert.assertEquals(response.jsonPath().getString("email"), createdEmail);
    Assert.assertEquals(response.jsonPath().getString("gender"), "male");
    Assert.assertEquals(response.jsonPath().getString("status"), "active");
  }

  @Feature("GoREST API")
  @Story("User CRUD")
  private void replaceUserStep() {
    Response response = client.authenticatedPut("/users/" + userId, Map.of(
        "name", "Someone Like You",
        "email", replacedEmail,
        "gender", "male",
        "status", "active"), token);

    Assert.assertEquals(response.statusCode(), 200, "Replace user status code should be 200.");
    Assert.assertEquals(response.jsonPath().getInt("id"), userId);
    Assert.assertEquals(response.jsonPath().getString("name"), "Someone Like You");
    Assert.assertEquals(response.jsonPath().getString("email"), replacedEmail);
    Assert.assertEquals(response.jsonPath().getString("gender"), "male");
    Assert.assertEquals(response.jsonPath().getString("status"), "active");
  }

  @Feature("GoREST API")
  @Story("Posts")
  private void createPostForUserStep() {
    Response response = client.authenticatedPost("/users/" + userId + "/posts", Map.of(
        "title", "Nihil accendo turba voluptatem totidem virtus vilis ut mollitia.",
        "body", "Alter non sumptus. Adicio tabula altus. Comedo suggero degero. Armarium cicuta corporis."),
        token);

    Assert.assertEquals(response.statusCode(), 201, "Create post status code should be 201.");
    postId = response.jsonPath().getInt("id");
    Assert.assertTrue(postId > 0, "Created post id should be present.");
    Assert.assertEquals(response.jsonPath().getInt("user_id"), userId);
    Assert.assertEquals(response.jsonPath().getString("title"),
        "Nihil accendo turba voluptatem totidem virtus vilis ut mollitia.");
    Assert.assertEquals(response.jsonPath().getString("body"),
        "Alter non sumptus. Adicio tabula altus. Comedo suggero degero. Armarium cicuta corporis.");
  }

  @Feature("GoREST API")
  @Story("Posts")
  private void getUserPostsStep() {
    Response response = client.authenticatedGet("/users/" + userId + "/posts", token);

    Assert.assertEquals(response.statusCode(), 200, "Get user posts status code should be 200.");
    List<Map<String, Object>> posts = response.jsonPath().getList("$");
    Assert.assertFalse(posts.isEmpty(), "At least one post should exist for the created user.");
    Map<String, Object> firstPost = posts.get(0);
    Assert.assertTrue(firstPost.containsKey("id"), "Post should contain id.");
    Assert.assertTrue(firstPost.containsKey("user_id"), "Post should contain user_id.");
    Assert.assertTrue(firstPost.containsKey("title"), "Post should contain title.");
    Assert.assertTrue(firstPost.containsKey("body"), "Post should contain body.");
    Assert.assertEquals(((Number) firstPost.get("user_id")).intValue(), userId);
  }

  @Feature("GoREST API")
  @Story("Posts")
  private void getSinglePostStep() {
    Response response = client.authenticatedGet("/posts/" + postId, token);

    Assert.assertEquals(response.statusCode(), 200, "Get single post status code should be 200.");
    Assert.assertEquals(response.jsonPath().getInt("id"), postId);
    Assert.assertEquals(response.jsonPath().getInt("user_id"), userId);
    Assert.assertNotNull(response.jsonPath().getString("title"), "Post title should be present.");
    Assert.assertNotNull(response.jsonPath().getString("body"), "Post body should be present.");
  }

  @Feature("GoREST API")
  @Story("Posts")
  private void updatePostStep() {
    Response response = client.authenticatedPatch("/posts/" + postId, Map.of("title", "Updated Post Title"), token);

    Assert.assertEquals(response.statusCode(), 200, "Update post status code should be 200.");
    Assert.assertEquals(response.jsonPath().getInt("id"), postId);
    Assert.assertEquals(response.jsonPath().getInt("user_id"), userId);
    Assert.assertEquals(response.jsonPath().getString("title"), "Updated Post Title");
    Assert.assertNotNull(response.jsonPath().getString("body"), "Post body should still be present.");
  }

  @Feature("GoREST API")
  @Story("Comments")
  private void createPostCommentStep() {
    Response response = client.authenticatedPost("/posts/" + postId + "/comments", Map.of(
        "name", "Bhagvan Chaturvedi",
        "email", "chaturvedi.bhagvan." + RandomData.alphaNumeric(8).toLowerCase() + "@example.test",
        "body", "Maxime dignissimos sint. Recusandae mollitia nesciunt."), token);

    Assert.assertEquals(response.statusCode(), 201, "Create comment status code should be 201.");
    commentId = response.jsonPath().getInt("id");
    Assert.assertTrue(commentId > 0, "Created comment id should be present.");
    Assert.assertEquals(response.jsonPath().getInt("post_id"), postId);
    Assert.assertEquals(response.jsonPath().getString("name"), "Bhagvan Chaturvedi");
    Assert.assertEquals(response.jsonPath().getString("body"),
        "Maxime dignissimos sint. Recusandae mollitia nesciunt.");
  }

  @Feature("GoREST API")
  @Story("Todos")
  private void createTodoStep() {
    String dueOn = OffsetDateTime.now().plusDays(7).withNano(0).toString();
    Response response = client.authenticatedPost("/users/" + userId + "/todos", Map.of(
        "title", "Tantum approbo communis doloribus avaritia.",
        "due_on", dueOn,
        "status", "pending"), token);

    Assert.assertEquals(response.statusCode(), 201, "Create todo status code should be 201.");
    todoId = response.jsonPath().getInt("id");
    Assert.assertTrue(todoId > 0, "Created todo id should be present.");
    Assert.assertEquals(response.jsonPath().getInt("user_id"), userId);
    Assert.assertEquals(response.jsonPath().getString("title"), "Tantum approbo communis doloribus avaritia.");
    Assert.assertEquals(response.jsonPath().getString("status"), "pending");
  }

  @Feature("GoREST API")
  @Story("Posts")
  private void deletePostStep() {
    Response response = client.authenticatedDelete("/posts/" + postId, token);

    Assert.assertEquals(response.statusCode(), 204, "Delete post status code should be 204.");
    Assert.assertTrue(response.body().asString().isBlank(), "Delete post response body should be empty.");
    Assert.assertEquals(client.authenticatedGet("/posts/" + postId, token).statusCode(), 404);
  }

  @Feature("GoREST API")
  @Story("User CRUD")
  private void deleteUserStep() {
    Response response = client.authenticatedDelete("/users/" + userId, token);

    Assert.assertEquals(response.statusCode(), 204, "Delete user status code should be 204.");
    Assert.assertTrue(response.body().asString().isBlank(), "Delete user response body should be empty.");
    Assert.assertEquals(client.authenticatedGet("/users/" + userId, token).statusCode(), 404);
  }
}
