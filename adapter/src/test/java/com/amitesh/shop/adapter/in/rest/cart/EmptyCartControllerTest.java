package com.amitesh.shop.adapter.in.rest.cart;

import static com.amitesh.shop.adapter.in.rest.helper.ControllerTestHelper.TEST_CUSTOMER_ID;
import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

import com.amitesh.shop.application.port.in.cart.EmptyCartUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test-with-mysql")
@SpringBootTest(webEnvironment = RANDOM_PORT)
class EmptyCartControllerTest {

  @LocalServerPort
  private Integer port;

  @MockBean
  private EmptyCartUseCase emptyCartUseCase;

  @Test
  void testEmptyCart_givenACustomerId_invokesDeleteCartUseCaseAndReturnsUpdatedCart() {

    given()
        .port(port)
        .delete("/carts/" + TEST_CUSTOMER_ID.value())
        .then()
        .statusCode(OK.value());

    verify(emptyCartUseCase).emptyCart(TEST_CUSTOMER_ID);
  }
}