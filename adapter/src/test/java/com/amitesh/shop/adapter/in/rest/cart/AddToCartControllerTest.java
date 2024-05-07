package com.amitesh.shop.adapter.in.rest.cart;

import static com.amitesh.shop.adapter.in.rest.helper.CartsControllerAssertions.assertThatResponseIsCart;
import static com.amitesh.shop.adapter.in.rest.helper.ControllerTestHelper.TEST_CUSTOMER_ID;
import static com.amitesh.shop.adapter.in.rest.helper.ControllerTestHelper.TEST_PRODUCT_1;
import static com.amitesh.shop.adapter.in.rest.helper.HttpTestHelper.assertThatResponseIsError;
import static com.amitesh.shop.model.ModelTestHelper.randomProductId;
import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.amitesh.shop.application.port.in.cart.AddToCartUseCase;
import com.amitesh.shop.application.port.in.cart.ProductNotFoundException;
import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.cart.InsufficientStockException;
import com.amitesh.shop.model.product.ProductId;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test-with-mysql")
@SpringBootTest(webEnvironment = RANDOM_PORT)
class AddToCartControllerTest {

  @LocalServerPort
  private Integer port;

  @MockBean
  private AddToCartUseCase addToCartUseCase;

  @Test
  void testAddToCart_givenSomeTestData_invokesAddToCartUseCaseAndReturnsUpdatedCart()
      throws InsufficientStockException, ProductNotFoundException {
    ProductId productId = TEST_PRODUCT_1.id();
    int quantity = 5;

    Cart cart = new Cart(TEST_CUSTOMER_ID);
    cart.addProduct(TEST_PRODUCT_1, quantity);

    when(addToCartUseCase.addToCart(TEST_CUSTOMER_ID, productId, quantity)).thenReturn(cart);

    Response response =
        given()
            .port(port)
            .queryParam("productId", productId.value())
            .queryParam("quantity", quantity)
            .post("/carts/" + TEST_CUSTOMER_ID.value() + "/line-items")
            .then()
            .extract()
            .response();

    assertThatResponseIsCart(response, cart);
  }

  @Test
  void testAddToCart_givenAnInvalidProductId_invokesAddToCartUseCaseAndReturnsAnError() {
    String productId = "";
    int quantity = 5;

    Response response =
        given()
            .port(port)
            .queryParam("productId", productId)
            .queryParam("quantity", quantity)
            .post("/carts/" + TEST_CUSTOMER_ID.value() + "/line-items")
            .then()
            .extract()
            .response();

    assertThatResponseIsError(response, BAD_REQUEST, "Missing 'productId'");
  }

  @Test
  void testAddToCart_givenProductNotFound_invokesAddToCartUseCaseAndReturnsAnError()
      throws InsufficientStockException, ProductNotFoundException {
    ProductId productId = randomProductId();
    int quantity = 5;

    when(addToCartUseCase.addToCart(TEST_CUSTOMER_ID, productId, quantity))
        .thenThrow(new ProductNotFoundException());

    Response response =
        given()
            .port(port)
            .queryParam("productId", productId.value())
            .queryParam("quantity", quantity)
            .post("/carts/" + TEST_CUSTOMER_ID.value() + "/line-items")
            .then()
            .extract()
            .response();

    assertThatResponseIsError(response, BAD_REQUEST, "The requested product does not exist");
  }

  @Test
  void testAddToCart_givenNotEnoughItemsInStock_invokesAddToCartUseCaseAndReturnsAnError()
      throws InsufficientStockException, ProductNotFoundException {
    ProductId productId = randomProductId();
    int quantity = 5;

    when(addToCartUseCase.addToCart(TEST_CUSTOMER_ID, productId, quantity))
        .thenThrow(new InsufficientStockException("Not enough items in stock", 2));

    Response response =
        given()
            .port(port)
            .queryParam("productId", productId.value())
            .queryParam("quantity", quantity)
            .post("/carts/" + TEST_CUSTOMER_ID.value() + "/line-items")
            .then()
            .extract()
            .response();

    assertThatResponseIsError(response, BAD_REQUEST, "Only 2 items in stock");
  }

}