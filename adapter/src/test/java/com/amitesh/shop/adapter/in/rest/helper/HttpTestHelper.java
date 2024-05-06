package com.amitesh.shop.adapter.in.rest.helper;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public final class HttpTestHelper {

  /* Different Port as the application runs on port 8080 */
  public static final int TEST_PORT = 8082;

  private HttpTestHelper() {
  }

  public static void assertThatResponseIsError(
      Response response,
      jakarta.ws.rs.core.Response.Status expectedStatus,
      String expectedErrorMessage) {
    assertThat(response.getStatusCode()).isEqualTo(expectedStatus.getStatusCode());

    JsonPath json = response.jsonPath();

    assertThat(json.getInt("httpStatus")).isEqualTo(expectedStatus.getStatusCode());
    assertThat(json.getString("errorMessage")).isEqualTo(expectedErrorMessage);
  }
}
