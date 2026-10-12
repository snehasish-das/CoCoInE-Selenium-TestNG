package org.cocoine.framework.api;

import static io.restassured.RestAssured.given;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import org.cocoine.framework.config.FrameworkConfig;

public class RestClient {
  private final RequestSpecification baseSpec;

  public RestClient() {
    RestAssuredConfig config = RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig()
        .setParam("http.connection.timeout", 30000)
        .setParam("http.socket.timeout", 30000));
    this.baseSpec = new RequestSpecBuilder()
        .setBaseUri(FrameworkConfig.restBaseUrl())
        .setContentType(ContentType.JSON)
        .setConfig(config)
        .addFilter(new AllureRestAssured())
        .build();
  }

  public Response get(String path) {
    return given(baseSpec).when().get(path).then().extract().response();
  }

  public Response post(String path, Object body) {
    return given(baseSpec).body(body).when().post(path).then().extract().response();
  }

  public Response put(String path, Object body) {
    return given(baseSpec).body(body).when().put(path).then().extract().response();
  }

  public Response patch(String path, Object body) {
    return given(baseSpec).body(body).when().patch(path).then().extract().response();
  }

  public Response delete(String path) {
    return given(baseSpec).when().delete(path).then().extract().response();
  }

  public Response authenticatedGet(String path, String bearerToken) {
    return given(baseSpec).auth().oauth2(bearerToken).when().get(path).then().extract().response();
  }

  public Response authenticatedPost(String path, Object body, String bearerToken) {
    return given(baseSpec).auth().oauth2(bearerToken).body(body).when().post(path).then().extract().response();
  }

  public Response authenticatedPut(String path, Object body, String bearerToken) {
    return given(baseSpec).auth().oauth2(bearerToken).body(body).when().put(path).then().extract().response();
  }

  public Response authenticatedPatch(String path, Object body, String bearerToken) {
    return given(baseSpec).auth().oauth2(bearerToken).body(body).when().patch(path).then().extract().response();
  }

  public Response authenticatedDelete(String path, String bearerToken) {
    return given(baseSpec).auth().oauth2(bearerToken).when().delete(path).then().extract().response();
  }

  public Response withHeadersGet(String path, Map<String, String> headers) {
    return given(baseSpec).headers(headers).when().get(path).then().extract().response();
  }
}
