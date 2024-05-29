package com.amitesh.shop.adapter.in.rest.cart;

import static com.amitesh.shop.adapter.in.rest.helper.CartsControllerAssertions.assertThatResponseIsCart;
import static com.amitesh.shop.adapter.in.rest.helper.ControllerTestHelper.TEST_CUSTOMER_ID;
import static com.amitesh.shop.adapter.in.rest.helper.ControllerTestHelper.TEST_PRODUCT_1;
import static com.amitesh.shop.adapter.in.rest.helper.ControllerTestHelper.TEST_PRODUCT_2;
import static com.amitesh.shop.adapter.in.rest.helper.HttpTestHelper.assertThatResponseIsError;
import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.amitesh.shop.application.port.in.cart.GetCartUseCase;
import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.cart.InsufficientStockException;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
class GetCartControllerTest {

  @LocalServerPort
  private Integer port;

  @MockBean
  private GetCartUseCase getCartUseCase;

  @Test
  void testGetCart_givenAnInvalidCustomerId_invokesGetCartUseCaseAndReturnsAnError() {
    String customerId = "foo";

    Response response =
        given().port(port).get("/carts/" + customerId).then().extract().response();

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
        given().port(port).get("/carts/" + TEST_CUSTOMER_ID.value()).then().extract()
            .response();

    assertThatResponseIsCart(response, cart);
  }

}