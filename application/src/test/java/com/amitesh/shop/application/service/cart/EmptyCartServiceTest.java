package com.amitesh.shop.application.service.cart;

import static com.amitesh.shop.application.service.ApplicationTestHelper.TEST_CUSTOMER_ID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.amitesh.shop.application.port.out.persistence.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmptyCartServiceTest {

  @Mock
  private CartRepository cartRepository;

  @InjectMocks
  private EmptyCartService emptyCartService;

  @Test
  void testEmptyCart_invalidCustomerId_throwsException() {
    assertThatThrownBy(
        () -> emptyCartService.emptyCart(null))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("'customerId' must not be null");

    verify(cartRepository, never()).deleteById(any());
  }

  @Test
  void testEmptyCart_invokesDeleteOnThePersistencePort() {
    emptyCartService.emptyCart(TEST_CUSTOMER_ID);

    verify(cartRepository).deleteById(TEST_CUSTOMER_ID);
  }

}