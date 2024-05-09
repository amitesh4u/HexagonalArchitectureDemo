package com.amitesh.shop.adapter.in.rest.product;

import static com.amitesh.shop.adapter.in.rest.helper.ControllerTestHelper.TEST_PRODUCT_1;
import static com.amitesh.shop.adapter.in.rest.helper.ControllerTestHelper.TEST_PRODUCT_2;
import static com.amitesh.shop.adapter.in.rest.helper.HttpTestHelper.assertThatResponseIsError;
import static com.amitesh.shop.adapter.in.rest.helper.ProductsControllerAssertions.assertThatResponseIsProductList;
import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.amitesh.shop.application.port.in.product.FindProductsUseCase;
import com.amitesh.shop.model.product.Product;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test-with-mysql")
@SpringBootTest(webEnvironment = RANDOM_PORT)
class ProductsControllerTest {

  @LocalServerPort
  private Integer port;

  @MockBean
  private FindProductsUseCase findProductsUseCase;

  @Test
  void testFindAllProducts_returnsAllProducts() {
    List<Product> productList = List.of(TEST_PRODUCT_1, TEST_PRODUCT_2);
    when(findProductsUseCase.findAll()).thenReturn(productList);

    Response response =
        given()
            .port(port)
            .get("/products/all")
            .then()
            .extract()
            .response();

    assertThatResponseIsProductList(response, productList);
  }

  @Test
  void testFindProducts_givenAQueryAndAListOfProducts_requestsProductsViaQueryAndReturnsThem() {
    String query = "foo";
    List<Product> productList = List.of(TEST_PRODUCT_1, TEST_PRODUCT_2);

    when(findProductsUseCase.findByNameOrDescription(query)).thenReturn(productList);

    Response response =
        given()
            .port(port)
            .queryParam("query", query)
            .get("/products")
            .then()
            .extract()
            .response();

    assertThatResponseIsProductList(response, productList);
  }

  @Test
  void testFindProducts_givenANullQuery_returnsError() {
    Response response = given().port(port).get("/products").then().extract().response();

    assertThatResponseIsError(response, BAD_REQUEST, "Missing 'query'");
  }

  @Test
  void testFindProducts_givenATooShortQuery_returnsError() {
    String query = "e";
    when(findProductsUseCase.findByNameOrDescription(query))
        .thenThrow(IllegalArgumentException.class);

    Response response =
        given()
            .port(port)
            .queryParam("query", query)
            .get("/products")
            .then()
            .extract()
            .response();

    assertThatResponseIsError(response, BAD_REQUEST, "Invalid 'query'");
  }
}