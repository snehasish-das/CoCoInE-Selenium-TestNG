package org.cocoine.pages.rest;

import io.restassured.response.Response;
import java.util.Map;
import org.cocoine.core.RestBaseClass;

public class SampleApi extends RestBaseClass {
  public Response getResource(String endpoint) {
    return get(endpoint);
  }

  public Response getResource(String endpoint, Map<String, String> headers, Map<String, ?> queryParams) {
    return get(endpoint, headers, queryParams, null);
  }

  public Response createResource(String endpoint, Object body, Map<String, String> headers, String bearerToken) {
    return post(endpoint, body, headers, bearerToken);
  }
}
