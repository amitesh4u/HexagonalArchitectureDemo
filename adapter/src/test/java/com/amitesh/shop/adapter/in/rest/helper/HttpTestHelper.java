package com.amitesh.shop.adapter.in.rest.helper;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

public final class HttpTestHelper {

  /* Different Port as the application runs on port 8080 */

  private HttpTestHelper() {
  }

  public static void assertThatResponseIsError(
      Response response,
      HttpStatus expectedStatus,
      String expectedErrorMessage) {
    assertThat(response.getStatusCode()).isEqualTo(expectedStatus.value());

    JsonPath json = response.jsonPath();

    assertThat(json.getInt("httpStatus")).isEqualTo(expectedStatus.value());
    assertThat(json.getString("errorMessage")).isEqualTo(expectedErrorMessage);
  }
}
