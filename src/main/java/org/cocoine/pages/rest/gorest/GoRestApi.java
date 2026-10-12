package org.cocoine.pages.rest.gorest;

import io.restassured.response.Response;
import org.cocoine.core.Config;
import org.cocoine.core.RestBaseClass;

public class GoRestApi extends RestBaseClass {
  public GoRestApi() {
    super(Config.value("Apis.gorest"));
  }

  public Response getUsers() {
    return get("/users");
  }

  public Response getUser(int userId) {
    return get("/users/" + userId);
  }

  public Response getPosts() {
    return get("/posts");
  }

  public Response getComments() {
    return get("/comments");
  }

  public Response getTodos() {
    return get("/todos");
  }
}
