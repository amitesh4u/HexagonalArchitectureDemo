package com.amitesh.shop.adapter.out.persistence;

import static com.amitesh.shop.model.ModelTestHelper.PRODUCT_QUANTITY_IN_STOCK;
import static com.amitesh.shop.model.ModelTestHelper.createTestProduct;
import static com.amitesh.shop.model.ModelTestHelper.euros;
import static org.assertj.core.api.Assertions.assertThat;

import com.amitesh.shop.application.port.out.persistence.CartRepository;
import com.amitesh.shop.application.port.out.persistence.ProductRepository;
import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.cart.CartLineItem;
import com.amitesh.shop.model.cart.InsufficientStockException;
import com.amitesh.shop.model.customer.CustomerId;
import com.amitesh.shop.model.product.Product;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractCartRepositoryTest {

  private static final Product TEST_PRODUCT_1 = createTestProduct(euros(19, 99),
      PRODUCT_QUANTITY_IN_STOCK);
  private static final Product TEST_PRODUCT_2 = createTestProduct(euros(1, 49),
      PRODUCT_QUANTITY_IN_STOCK);

  private static final AtomicInteger CUSTOMER_ID_SEQUENCE_GENERATOR = new AtomicInteger();

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private ProductRepository productRepository;

  private static CustomerId createUniqueCustomerId() {
    return new CustomerId(CUSTOMER_ID_SEQUENCE_GENERATOR.incrementAndGet());
  }

  @BeforeEach
  void initRepositories() {
    persistTestProducts();
  }

  private void persistTestProducts() {
    productRepository.save(TEST_PRODUCT_1);
    productRepository.save(TEST_PRODUCT_2);
  }

  @Test
  void testFindByCustomerId_givenACustomerIdWithNoExistingCart_returnsAnEmptyCart() {
    CustomerId customerId = createUniqueCustomerId();

    Optional<Cart> cart = cartRepository.findByCustomerId(customerId);

    assertThat(cart).isEmpty();
  }

  @Test
  void testFindByCustomerId_givenPersistedCartWithProduct_returnsTheAppropriateCart()
      throws InsufficientStockException {
    CustomerId customerId = createUniqueCustomerId();

    Cart persistedCart = new Cart(customerId);
    persistedCart.addProduct(TEST_PRODUCT_1, 1);
    cartRepository.save(persistedCart);

    Optional<Cart> cart = cartRepository.findByCustomerId(customerId);

    assertThat(cart).isNotEmpty();
    assertThat(cart.get().id()).isEqualTo(customerId);
    assertThat(cart.get().lineItems()).hasSize(1);
    assertThat(cart.get().lineItems().getFirst().product()).isEqualTo(TEST_PRODUCT_1);
    assertThat(cart.get().lineItems().getFirst().quantity()).isEqualTo(1);
  }

  @Test
  void testSave_givenExistingCartWithProduct_andGivenANewCartForTheSameCustomer_overwritesTheExistingCart()
      throws InsufficientStockException {
    CustomerId customerId = createUniqueCustomerId();

    Cart existingCart = new Cart(customerId);
    existingCart.addProduct(TEST_PRODUCT_1, 1);
    cartRepository.save(existingCart);

    Cart newCart = new Cart(customerId);
    newCart.addProduct(TEST_PRODUCT_2, 2);
    cartRepository.save(newCart);

    Optional<Cart> cart = cartRepository.findByCustomerId(customerId);
    assertThat(cart).isNotEmpty();
    assertThat(cart.get().id()).isEqualTo(customerId);
    assertThat(cart.get().lineItems()).hasSize(1);
    assertThat(cart.get().lineItems().getFirst().product()).isEqualTo(TEST_PRODUCT_2);
    assertThat(cart.get().lineItems().getFirst().quantity()).isEqualTo(2);
  }

  @Test
  void testSave_givenExistingCartWithProduct_addProductAndSaveCart_updatesTheExistingCart()
      throws InsufficientStockException {
    CustomerId customerId = createUniqueCustomerId();

    Cart existingCart = new Cart(customerId);
    existingCart.addProduct(TEST_PRODUCT_1, 1);
    cartRepository.save(existingCart);

    existingCart = cartRepository.findByCustomerId(customerId).orElseThrow();
    existingCart.addProduct(TEST_PRODUCT_2, 2);
    cartRepository.save(existingCart);

    Optional<Cart> cart = cartRepository.findByCustomerId(customerId);
    assertThat(cart).isNotEmpty();
    assertThat(cart.get().id()).isEqualTo(customerId);
    assertThat(cart.get().lineItems())
        .map(CartLineItem::product)
        .containsExactlyInAnyOrder(TEST_PRODUCT_1, TEST_PRODUCT_2);
  }

  @Test
  void testDeleteByCustomerId_givenExistingCart_deletesTheCart() {
    CustomerId customerId = createUniqueCustomerId();

    Cart existingCart = new Cart(customerId);
    cartRepository.save(existingCart);

    assertThat(cartRepository.findByCustomerId(customerId)).isNotEmpty();

    cartRepository.deleteByCustomerId(customerId);

    assertThat(cartRepository.findByCustomerId(customerId)).isEmpty();
  }

  @Test
  void testDeleteByCustomerId_givenNonExistingCart_doesNothing() {
    CustomerId customerId = createUniqueCustomerId();
    assertThat(cartRepository.findByCustomerId(customerId)).isEmpty();

    cartRepository.deleteByCustomerId(customerId);

    assertThat(cartRepository.findByCustomerId(customerId)).isEmpty();
  }
}
