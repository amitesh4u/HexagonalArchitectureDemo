package com.amitesh.shop.model.cart;

import static com.amitesh.shop.model.ModelTestHelper.PRODUCT_QUANTITY_IN_STOCK;
import static com.amitesh.shop.model.ModelTestHelper.RANDOM_PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.amitesh.shop.model.price.Price;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CartLineItemTest {

  public static final int INITIAL_CART_ITEM_QUANTITY = 20;

  @ParameterizedTest(name = "#{index} - Run test with args={0}")
  @ValueSource(ints = {-100, -1, 0})
  void testIncreaseQuantityBy_invalidNewQuantity_throwsException(int augend) {
    CartLineItem cartLineItem = new CartLineItem(RANDOM_PRODUCT);
    assertThatThrownBy(
        () -> cartLineItem.increaseQuantityBy(augend, PRODUCT_QUANTITY_IN_STOCK)
    ).isExactlyInstanceOf(
        IllegalArgumentException.class).hasMessage("You must add at least one item");
  }

  @ParameterizedTest(name = "#{index} - Run test with args={0}")
  @ValueSource(ints = {100, 150})
  void testIncreaseQuantityBy_validNewQuantity_itemOutOfStock_throwsException(int augend)
      throws OutOfStockException {
    CartLineItem cartLineItem = new CartLineItem(RANDOM_PRODUCT);
    cartLineItem.increaseQuantityBy(INITIAL_CART_ITEM_QUANTITY, PRODUCT_QUANTITY_IN_STOCK);
    assertThatThrownBy(
        () -> cartLineItem.increaseQuantityBy(augend, PRODUCT_QUANTITY_IN_STOCK)
    ).isExactlyInstanceOf(OutOfStockException.class);
  }

  @ParameterizedTest(name = "#{index} - Run test with args={0}")
  @ValueSource(ints = {50, 1})
  void testIncreaseQuantityBy_validNewQuantity_itemInStock_returnUpdatedObject(int augend)
      throws OutOfStockException {
    CartLineItem cartLineItem = new CartLineItem(RANDOM_PRODUCT);
    cartLineItem.increaseQuantityBy(INITIAL_CART_ITEM_QUANTITY, PRODUCT_QUANTITY_IN_STOCK);
    cartLineItem.increaseQuantityBy(augend, PRODUCT_QUANTITY_IN_STOCK);
    assertThat(cartLineItem.quantity()).isEqualTo(INITIAL_CART_ITEM_QUANTITY + augend);
  }

  @Test
  void testSubTotal_validValues_returnValidTotal() throws OutOfStockException {
    CartLineItem cartLineItem = new CartLineItem(RANDOM_PRODUCT);
    cartLineItem.increaseQuantityBy(INITIAL_CART_ITEM_QUANTITY, PRODUCT_QUANTITY_IN_STOCK);
    Price subTotal = cartLineItem.subTotal();
    assertThat(subTotal.amount()).isEqualTo(
        RANDOM_PRODUCT.price().amount().multiply(BigDecimal.valueOf(INITIAL_CART_ITEM_QUANTITY)));
  }
}