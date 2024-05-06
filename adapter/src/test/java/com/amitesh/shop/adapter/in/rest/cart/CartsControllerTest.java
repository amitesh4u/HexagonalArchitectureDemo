package com.amitesh.shop.adapter.in.rest.cart;

import static com.amitesh.shop.adapter.in.rest.helper.CartsControllerAssertions.assertThatResponseIsCart;
import static com.amitesh.shop.adapter.in.rest.helper.ControllerTestHelper.TEST_CUSTOMER_ID;
import static com.amitesh.shop.adapter.in.rest.helper.ControllerTestHelper.TEST_PRODUCT_1;
import static com.amitesh.shop.adapter.in.rest.helper.ControllerTestHelper.TEST_PRODUCT_2;
import static com.amitesh.shop.adapter.in.rest.helper.HttpTestHelper.TEST_PORT;
import static com.amitesh.shop.adapter.in.rest.helper.HttpTestHelper.assertThatResponseIsError;
import static com.amitesh.shop.model.ModelTestHelper.randomProductId;
import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amitesh.shop.application.port.in.cart.AddToCartUseCase;
import com.amitesh.shop.application.port.in.cart.EmptyCartUseCase;
import com.amitesh.shop.application.port.in.cart.GetCartUseCase;
import com.amitesh.shop.application.port.in.cart.ProductNotFoundException;
import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.cart.InsufficientStockException;
import com.amitesh.shop.model.product.ProductId;
import io.restassured.response.Response;
import jakarta.ws.rs.core.Application;
import java.util.Set;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Single Test class for all Cart operations since Undertow server takes time and memory to start
 * and run. Will be broken into separate Test classes for each Controller with Spring Boot
 */
class CartsControllerTest {

  private static final AddToCartUseCase addToCartUseCase = mock(AddToCartUseCase.class);
  private static final GetCartUseCase getCartUseCase = mock(GetCartUseCase.class);
  private static final EmptyCartUseCase emptyCartUseCase = mock(EmptyCartUseCase.class);

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
                    return Set.of(
                        new AddToCartController(addToCartUseCase),
                        new GetCartController(getCartUseCase),
                        new EmptyCartController(emptyCartUseCase));
                  }
                });
  }

  @AfterAll
  static void stop() {
    server.stop();
  }

  @BeforeEach
  void resetMocks() {
    reset(addToCartUseCase, getCartUseCase, emptyCartUseCase);
  }

  @Test
  void testGetCart_givenAnInvalidCustomerId_invokesGetCartUseCaseAndReturnsAnError() {
    String customerId = "foo";

    Response response =
        given().port(TEST_PORT).get("/carts/" + customerId).then().extract().response();

    assertThatResponseIsError(response, BAD_REQUEST, "Invalid 'customerId'");
  }

  @Test
  void testGetCart_givenAValidCustomerIdAndACart_invokesGetCartUseCaseAndRequestsCartFromUseCaseAndReturnsIt()
      throws InsufficientStockException {

    Cart cart = new Cart(TEST_CUSTOMER_ID);
    cart.addProduct(TEST_PRODUCT_1, 3);
    cart.addProduct(TEST_PRODUCT_2, 5);

    when(getCartUseCase.getCart(TEST_CUSTOMER_ID)).thenReturn(cart);

    Response response =
        given().port(TEST_PORT).get("/carts/" + TEST_CUSTOMER_ID.value()).then().extract()
            .response();

    assertThatResponseIsCart(response, cart);
  }

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
            .port(TEST_PORT)
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
            .port(TEST_PORT)
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
            .port(TEST_PORT)
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
            .port(TEST_PORT)
            .queryParam("productId", productId.value())
            .queryParam("quantity", quantity)
            .post("/carts/" + TEST_CUSTOMER_ID.value() + "/line-items")
            .then()
            .extract()
            .response();

    assertThatResponseIsError(response, BAD_REQUEST, "Only 2 items in stock");
  }

  @Test
  void testEmptyCart_givenACustomerId_invokesDeleteCartUseCaseAndReturnsUpdatedCart() {

    given()
        .port(TEST_PORT)
        .delete("/carts/" + TEST_CUSTOMER_ID.value())
        .then()
        .statusCode(NO_CONTENT.getStatusCode());

    verify(emptyCartUseCase).emptyCart(TEST_CUSTOMER_ID);
  }
}