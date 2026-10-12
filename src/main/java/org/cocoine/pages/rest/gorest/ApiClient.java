package org.cocoine.pages.rest.gorest;

import io.restassured.response.Response;
import org.cocoine.core.Config;
import org.cocoine.core.RestBaseClass;

public class ApiClient extends RestBaseClass {
  public ApiClient() {
    super(Config.value("Apis.gorest"));
  }

  public Response getUsers() {
    return get("/users");
  }

  public Response getUser(int userId) {
    return get("/users/" + userId);
  }

  public Response getUserPosts(int userId) {
    return get("/users/" + userId + "/posts");
  }

  public Response getUserTodos(int userId) {
    return get("/users/" + userId + "/todos");
  }

  public Response getPosts() {
    return get("/posts");
  }

  public Response getPost(int postId) {
    return get("/posts/" + postId);
  }

  public Response getPostComments(int postId) {
    return get("/posts/" + postId + "/comments");
  }

  public Response getComments() {
    return get("/comments");
  }

  public Response getComment(int commentId) {
    return get("/comments/" + commentId);
  }

  public Response getTodos() {
    return get("/todos");
  }

  public Response getTodo(int todoId) {
    return get("/todos/" + todoId);
  }
}
