package org.cocoine.tests.api.gorest;

import io.restassured.response.Response;
import org.cocoine.pages.rest.gorest.ApiClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetSingleTodoTest {
  private final ApiClient api = new ApiClient();
  private int todoId;

  @Test(groups = {"api"}, description = "API TC_08 - Pick a todo id from the list")
  public void pickTodoId() {
    todoId = api.getTodos().jsonPath().getInt("[0].id");
    Assert.assertTrue(todoId > 0, "Todo id should be available.");
  }

  @Test(dependsOnMethods = "pickTodoId", groups = {"api"}, description = "API TC_08 - Get single todo")
  public void getSingleTodo() {
    Response response = api.getTodo(todoId);

    Assert.assertEquals(response.statusCode(), 200, "Single todo status should be 200.");
    Assert.assertEquals(response.jsonPath().getInt("id"), todoId, "Returned todo id should match.");
    Assert.assertNotNull(response.jsonPath().getString("status"), "Todo status should be present.");
  }
}
