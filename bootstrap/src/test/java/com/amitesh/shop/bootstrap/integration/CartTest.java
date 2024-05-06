package com.amitesh.shop.bootstrap.integration;

import static com.amitesh.shop.adapter.in.rest.helper.CartsControllerAssertions.assertThatResponseIsCart;
import static com.amitesh.shop.adapter.in.rest.helper.HttpTestHelper.TEST_PORT;
import static com.amitesh.shop.adapter.out.persistence.inmemory.TestProducts.LED_LIGHTS;
import static com.amitesh.shop.adapter.out.persistence.inmemory.TestProducts.MONITOR_DESK_MOUNT;
import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;

import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.cart.InsufficientStockException;
import com.amitesh.shop.model.customer.CustomerId;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CartTest extends EndToEndTest {

  private static final CustomerId TEST_CUSTOMER_ID = new CustomerId(61157);
  private static final String CARTS_PATH = "/carts/" + TEST_CUSTOMER_ID.value();

  @Test
  @Order(1)
  void testAddToCart_givenAnEmptyCart_addsTheLineItemAndReturnsUpdatedCart()
      throws InsufficientStockException {
    Response response =
        given()
            .port(TEST_PORT)
            .queryParam("productId", LED_LIGHTS.id().value())
            .queryParam("quantity", 3)
            .post(CARTS_PATH + "/line-items")
            .then()
            .extract()
            .response();

    Cart expectedCart = new Cart(TEST_CUSTOMER_ID);
    expectedCart.addProduct(LED_LIGHTS, 3);

    assertThatResponseIsCart(response, expectedCart);
  }

  @Test
  @Order(2)
  void testAddToCart_givenACartWithOneLineItem_addsTheItemAndReturnsUpdatedCart()
      throws InsufficientStockException {
    Response response =
        given()
            .port(TEST_PORT)
            .queryParam("productId", MONITOR_DESK_MOUNT.id().value())
            .queryParam("quantity", 1)
            .post(CARTS_PATH + "/line-items")
            .then()
            .extract()
            .response();

    Cart expectedCart = new Cart(TEST_CUSTOMER_ID);
    expectedCart.addProduct(LED_LIGHTS, 3);
    expectedCart.addProduct(MONITOR_DESK_MOUNT, 1);

    assertThatResponseIsCart(response, expectedCart);
  }

  @Test
  @Order(3)
  void testGetCart_givenACartWithTwoLineItems_returnsTheCart() throws InsufficientStockException {
    Response response = given().port(TEST_PORT).get(CARTS_PATH).then().extract().response();

    Cart expectedCart = new Cart(TEST_CUSTOMER_ID);
    expectedCart.addProduct(LED_LIGHTS, 3);
    expectedCart.addProduct(MONITOR_DESK_MOUNT, 1);

    assertThatResponseIsCart(response, expectedCart);
  }

  @Test
  @Order(4)
  void testDeleteCart_givenACartWithTwoLineItems_returnsStatusCodeNoContent() {
    given().port(TEST_PORT).delete(CARTS_PATH).then().statusCode(NO_CONTENT.getStatusCode());
  }

  @Test
  @Order(5)
  void testGetCart_givenAnEmptiedCart_returnsAnEmptyCart() {
    Response response = given().port(TEST_PORT).get(CARTS_PATH).then().extract().response();

    assertThatResponseIsCart(response, new Cart(TEST_CUSTOMER_ID));
  }
}
