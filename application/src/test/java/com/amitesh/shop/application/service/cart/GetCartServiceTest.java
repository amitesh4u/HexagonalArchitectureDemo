package com.amitesh.shop.application.service.cart;

import static com.amitesh.shop.application.service.ApplicationTestHelper.TEST_CUSTOMER_ID;
import static com.amitesh.shop.application.service.ApplicationTestHelper.TEST_PRODUCT_1;
import static com.amitesh.shop.application.service.ApplicationTestHelper.TEST_PRODUCT_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amitesh.shop.application.port.out.persistence.CartRepository;
import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.cart.InsufficientStockException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetCartServiceTest {

  @Mock
  private CartRepository cartRepository;

  @InjectMocks
  private GetCartService getCartService;

  @Test
  void testGetCart_invalidCustomerId_throwsException() {
    assertThatThrownBy(
        () -> getCartService.getCart(null))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("'customerId' must not be null");

    verify(cartRepository, never()).findByCustomerId(any());
  }

  @Test
  void testGetCart_cartIsPersisted_returnsCart() throws InsufficientStockException {
    Cart persistedCart = new Cart(TEST_CUSTOMER_ID);
    persistedCart.addProduct(TEST_PRODUCT_1, 1);
    persistedCart.addProduct(TEST_PRODUCT_2, 5);

    when(cartRepository.findByCustomerId(TEST_CUSTOMER_ID))
        .thenReturn(Optional.of(persistedCart));

    Cart cart = getCartService.getCart(TEST_CUSTOMER_ID);

    assertThat(cart).isSameAs(persistedCart);

    verify(cartRepository).findByCustomerId(TEST_CUSTOMER_ID);
  }

  @Test
  void testGetCart_cartIsNotPersisted_returnAnEmptyCart() {
    when(cartRepository.findByCustomerId(TEST_CUSTOMER_ID)).thenReturn(Optional.empty());

    Cart cart = getCartService.getCart(TEST_CUSTOMER_ID);

    assertThat(cart.lineItems()).isEmpty();

    verify(cartRepository).findByCustomerId(TEST_CUSTOMER_ID);
  }
}