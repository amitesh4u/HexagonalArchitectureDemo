package com.amitesh.shop.bootstrap.integration;

import static com.amitesh.shop.adapter.in.rest.helper.HttpTestHelper.TEST_PORT;
import static com.amitesh.shop.adapter.in.rest.helper.ProductsControllerAssertions.assertThatResponseIsProductList;
import static com.amitesh.shop.adapter.out.persistence.inmemory.TestProducts.COMPUTER_MONITOR;
import static com.amitesh.shop.adapter.out.persistence.inmemory.TestProducts.MONITOR_DESK_MOUNT;
import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Test;

class FindProductsTest extends EndToEndTest {

  @Test
  void testFindProducts_givenTestProductsAndAQuery_returnsMatchingProducts() {
    String query = "monitor";

    Response response =
        given()
            .port(TEST_PORT)
            .queryParam("query", query)
            .get("/products")
            .then()
            .extract()
            .response();

    assertThatResponseIsProductList(response, List.of(COMPUTER_MONITOR, MONITOR_DESK_MOUNT));
  }
}
