package org.cocoine.tests.api.gorest;

import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.cocoine.pages.rest.gorest.ApiClient;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetTodosListTest {
  private final ApiClient api = new ApiClient();

  @Test(groups = {"api"}, description = "API TC_07 - Get todos list")
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
