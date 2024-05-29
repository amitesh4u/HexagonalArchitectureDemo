package com.amitesh.shop.bootstrap.integration;

import static com.amitesh.shop.adapter.in.rest.helper.ProductsControllerAssertions.assertThatResponseIsProductList;
import static com.amitesh.shop.adapter.out.persistence.TestProducts.COMPUTER_MONITOR;
import static com.amitesh.shop.adapter.out.persistence.TestProducts.MONITOR_DESK_MOUNT;
import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FindProductsTest {

  @LocalServerPort
  private Integer port;

  @Test
  void testFindProducts_givenTestProductsAndAQuery_returnsMatchingProducts() {
    String query = "monitor";

    Response response =
        given()
            .port(port)
            .queryParam("query", query)
            .get("/products")
            .then()
            .extract()
            .response();

    assertThatResponseIsProductList(response, List.of(COMPUTER_MONITOR, MONITOR_DESK_MOUNT));
  }
}
