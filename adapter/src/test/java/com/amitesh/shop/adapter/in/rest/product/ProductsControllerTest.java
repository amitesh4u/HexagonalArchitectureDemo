package com.amitesh.shop.adapter.in.rest.product;

import static com.amitesh.shop.adapter.in.rest.helper.ControllerTestHelper.TEST_PRODUCT_1;
import static com.amitesh.shop.adapter.in.rest.helper.ControllerTestHelper.TEST_PRODUCT_2;
import static com.amitesh.shop.adapter.in.rest.helper.HttpTestHelper.TEST_PORT;
import static com.amitesh.shop.adapter.in.rest.helper.HttpTestHelper.assertThatResponseIsError;
import static com.amitesh.shop.adapter.in.rest.helper.ProductsControllerAssertions.assertThatResponseIsProductList;
import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import com.amitesh.shop.application.port.in.product.FindProductsUseCase;
import com.amitesh.shop.model.product.Product;
import io.restassured.response.Response;
import jakarta.ws.rs.core.Application;
import java.util.List;
import java.util.Set;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Single Test class for all Product operations since Undertow server takes time and memory to start and run.
 * Will be broken into separate Test classes for each Controller with Spring Boot
 */
@ExtendWith(MockitoExtension.class)
class ProductsControllerTest {

  private static final FindProductsUseCase findProductsUseCase = mock(FindProductsUseCase.class);

  private static UndertowJaxrsServer server;

  @BeforeAll
  static void init() {
    server =
        new UndertowJaxrsServer()
            .setPort(TEST_PORT)
            .start()
            .deploy(
                new Application() {
                  @Override
                  public Set<Object> getSingletons() {
                    return Set.of(new FindProductsController(findProductsUseCase));
                  }
                });
  }

  @AfterAll
  static void stop() {
    server.stop();
  }

  @BeforeEach
  void resetMocks() {
    reset(findProductsUseCase);
  }

  @Test
  void testFindProducts_givenAQueryAndAListOfProducts_requestsProductsViaQueryAndReturnsThem() {
    String query = "foo";
    List<Product> productList = List.of(TEST_PRODUCT_1, TEST_PRODUCT_2);

    when(findProductsUseCase.findByNameOrDescription(query)).thenReturn(productList);

    Response response =
        given()
            .port(TEST_PORT)
            .queryParam("query", query)
            .get("/products")
            .then()
            .extract()
            .response();

    assertThatResponseIsProductList(response, productList);
  }

  @Test
  void testFindProducts_givenANullQuery_returnsError() {
    Response response = given().port(TEST_PORT).get("/products").then().extract().response();

    assertThatResponseIsError(response, BAD_REQUEST, "Missing 'query'");
  }

  @Test
  void testFindProducts_givenATooShortQuery_returnsError() {
    String query = "e";
    when(findProductsUseCase.findByNameOrDescription(query))
        .thenThrow(IllegalArgumentException.class);

    Response response =
        given()
            .port(TEST_PORT)
            .queryParam("query", query)
            .get("/products")
            .then()
            .extract()
            .response();

    assertThatResponseIsError(response, BAD_REQUEST, "Invalid 'query'");
  }
}