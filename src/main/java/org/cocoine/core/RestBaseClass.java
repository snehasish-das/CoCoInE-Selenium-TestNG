package org.cocoine.core;

import static io.restassured.RestAssured.given;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Collections;
import java.util.Map;

public class RestBaseClass {
  private final String baseUrl;

  public RestBaseClass() {
    this(Config.apiBaseUrl());
  }

  public RestBaseClass(String baseUrl) {
    if (baseUrl == null || baseUrl.isBlank()) {
      throw new IllegalArgumentException("API base URL is not configured. Set Environment.apiBaseUrl or -DapiBaseUrl.");
    }
    this.baseUrl = baseUrl;
  }

  protected Response get(String endpoint) {
    return get(endpoint, Collections.emptyMap(), Collections.emptyMap(), null);
  }

  protected Response get(String endpoint, Map<String, String> headers) {
    return get(endpoint, headers, Collections.emptyMap(), null);
  }

  protected Response get(String endpoint, Map<String, String> headers, Map<String, ?> queryParams, String bearerToken) {
    return request(headers, queryParams, bearerToken).when().get(endpoint).then().extract().response();
  }

  protected Response post(String endpoint, Object body) {
    return post(endpoint, body, Collections.emptyMap(), null);
  }

  protected Response post(String endpoint, Object body, Map<String, String> headers, String bearerToken) {
    return request(headers, Collections.emptyMap(), bearerToken).body(body).when().post(endpoint).then().extract().response();
  }

  protected Response put(String endpoint, Object body, Map<String, String> headers, String bearerToken) {
    return request(headers, Collections.emptyMap(), bearerToken).body(body).when().put(endpoint).then().extract().response();
  }

  protected Response patch(String endpoint, Object body, Map<String, String> headers, String bearerToken) {
    return request(headers, Collections.emptyMap(), bearerToken).body(body).when().patch(endpoint).then().extract().response();
  }

  protected Response delete(String endpoint, Map<String, String> headers, String bearerToken) {
    return request(headers, Collections.emptyMap(), bearerToken).when().delete(endpoint).then().extract().response();
  }

  private RequestSpecification request(Map<String, String> headers, Map<String, ?> queryParams, String bearerToken) {
    RequestSpecification request = given(baseSpec()).headers(headers).queryParams(queryParams);
    if (bearerToken != null && !bearerToken.isBlank()) {
      request.auth().oauth2(bearerToken);
    }
    return request;
  }

  private RequestSpecification baseSpec() {
    RestAssuredConfig config = RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig()
        .setParam("http.connection.timeout", 30000)
        .setParam("http.socket.timeout", 30000));
    return new RequestSpecBuilder()
        .setBaseUri(baseUrl)
        .setContentType(ContentType.JSON)
        .setConfig(config)
        .build();
  }
}
