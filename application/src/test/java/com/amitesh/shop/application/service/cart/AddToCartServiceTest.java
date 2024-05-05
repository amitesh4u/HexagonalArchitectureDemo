package com.amitesh.shop.application.service.cart;

import static com.amitesh.shop.application.service.ApplicationTestHelper.TEST_CUSTOMER_ID;
import static com.amitesh.shop.application.service.ApplicationTestHelper.TEST_PRODUCT_1;
import static com.amitesh.shop.application.service.ApplicationTestHelper.TEST_PRODUCT_2;
import static com.amitesh.shop.model.ModelTestHelper.randomProductId;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amitesh.shop.application.port.in.cart.ProductNotFoundException;
import com.amitesh.shop.application.port.out.persistence.CartRepository;
import com.amitesh.shop.application.port.out.persistence.ProductRepository;
import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.cart.CartLineItem;
import com.amitesh.shop.model.cart.InsufficientStockException;
import com.amitesh.shop.model.product.ProductId;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
class AddToCartServiceTest {

  @InjectSoftAssertions
  private SoftAssertions softly;
  @Mock
  private CartRepository cartRepository;

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private AddToCartService addToCartService;

  @Test
  void testAddToCart_existingCart_addProductToCart_returnUpdatedCart()
      throws InsufficientStockException, ProductNotFoundException {
    Cart persistedCart = new Cart(TEST_CUSTOMER_ID);
    persistedCart.addProduct(TEST_PRODUCT_1, 1);

    when(cartRepository.findByCustomerId(TEST_CUSTOMER_ID)).thenReturn(Optional.of(persistedCart));
    when(productRepository.findById(TEST_PRODUCT_2.id())).thenReturn(Optional.of(TEST_PRODUCT_2));

    Cart cart = addToCartService.addToCart(TEST_CUSTOMER_ID, TEST_PRODUCT_2.id(), 3);

    verify(productRepository).findById(TEST_PRODUCT_2.id());
    verify(cartRepository).findByCustomerId(TEST_CUSTOMER_ID);
    verify(cartRepository).save(cart);

    List<CartLineItem> cartLineItems = cart.lineItems();
    softly.assertThat(cartLineItems).hasSize(2);
    softly.assertThat(cartLineItems.get(0).product()).isEqualTo(TEST_PRODUCT_1);
    softly.assertThat(cartLineItems.get(0).quantity()).isEqualTo(1);
    softly.assertThat(cartLineItems.get(1).product()).isEqualTo(TEST_PRODUCT_2);
    softly.assertThat(cartLineItems.get(1).quantity()).isEqualTo(3);
  }

  @Test
  void testAddToCart_noExistingCart_addProductsToCart_returnUpdatedCart()
      throws InsufficientStockException, ProductNotFoundException {
    when(productRepository.findById(TEST_PRODUCT_1.id())).thenReturn(Optional.of(TEST_PRODUCT_1));

    Cart cart = addToCartService.addToCart(TEST_CUSTOMER_ID, TEST_PRODUCT_1.id(), 2);

    verify(productRepository).findById(TEST_PRODUCT_1.id());
    verify(cartRepository).findByCustomerId(TEST_CUSTOMER_ID);
    verify(cartRepository).save(cart);

    List<CartLineItem> cartLineItems = cart.lineItems();
    softly.assertThat(cartLineItems).hasSize(1);
    softly.assertThat(cartLineItems.getFirst().product()).isEqualTo(TEST_PRODUCT_1);
    softly.assertThat(cartLineItems.getFirst().quantity()).isEqualTo(2);
  }

  @Test
  void testAddToCart_addUnknownProduct_throwsException() {
    ProductId productId = randomProductId();

    ThrowingCallable invocation = () -> addToCartService.addToCart(TEST_CUSTOMER_ID, productId, 1);

    assertThatExceptionOfType(ProductNotFoundException.class).isThrownBy(invocation);

    verify(productRepository).findById(productId);
    verify(cartRepository, never()).findByCustomerId(any());
    verify(cartRepository, never()).save(any());
  }

  @Test
  void testAddToCart_invalidQuantity_throwsException() {
    int quantity = 0;

    assertThatThrownBy(
        () -> addToCartService.addToCart(TEST_CUSTOMER_ID, TEST_PRODUCT_1.id(), quantity))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("'quantity' must be greater than 0");

    verify(productRepository, never()).findById(any());
    verify(cartRepository, never()).findByCustomerId(any());
    verify(cartRepository, never()).save(any());
  }

  @Test
  void testAddToCart_invalidCustomerId_throwsException() {
    assertThatThrownBy(
        () -> addToCartService.addToCart(null, TEST_PRODUCT_1.id(), 1))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("'customerId' must not be null");

    verify(productRepository, never()).findById(any());
    verify(cartRepository, never()).findByCustomerId(any());
    verify(cartRepository, never()).save(any());
  }

  @Test
  void testAddToCart_invalidProductId_throwsException() {
    assertThatThrownBy(
        () -> addToCartService.addToCart(TEST_CUSTOMER_ID, null, 1))
        .isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("'productId' must not be null");

    verify(productRepository, never()).findById(any());
    verify(cartRepository, never()).findByCustomerId(any());
    verify(cartRepository, never()).save(any());
  }

}