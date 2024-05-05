package com.amitesh.shop.model.cart;

import static com.amitesh.shop.model.ModelTestHelper.PRODUCT_QUANTITY_IN_STOCK;
import static com.amitesh.shop.model.ModelTestHelper.createTestProduct;
import static com.amitesh.shop.model.ModelTestHelper.emptyCartForRandomCustomer;
import static com.amitesh.shop.model.ModelTestHelper.euros;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.amitesh.shop.model.product.Product;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@ExtendWith(SoftAssertionsExtension.class)
class CartTest {

  @InjectSoftAssertions
  private SoftAssertions softly;

  @Test
  void testAddProduct_givenEmptyCart_addTwoProducts_productsAreInCart() throws InsufficientStockException {
    Cart cart = emptyCartForRandomCustomer();

    Product product1 = createTestProduct(euros(12, 99), PRODUCT_QUANTITY_IN_STOCK);

    Product product2 = createTestProduct(euros(5, 97), PRODUCT_QUANTITY_IN_STOCK);

    int product1Quantity = 3;
    int product2Quantity = 5;
    cart.addProduct(product1, product1Quantity);
    cart.addProduct(product2, product2Quantity);

    softly.assertThat(cart.numberOfItems()).isEqualTo(product1Quantity + product2Quantity);
    softly.assertThat(cart.lineItems()).hasSize(2);
    softly.assertThat(cart.lineItems().get(0).product()).isEqualTo(product1);
    softly.assertThat(cart.lineItems().get(0).quantity()).isEqualTo(product1Quantity);
    softly.assertThat(cart.lineItems().get(1).product()).isEqualTo(product2);
    softly.assertThat(cart.lineItems().get(1).quantity()).isEqualTo(product2Quantity);
    softly.assertThat(cart.subTotal())
        .isEqualTo(product1.price().multiply(product1Quantity).add(product2.price().multiply(
            product2Quantity)));
  }

  @Test
  void testAddProduct_givenEmptyCart_addTwoProducts_validNumberOfItemsAndSubTotal()
      throws InsufficientStockException {
    Cart cart = emptyCartForRandomCustomer();

    Product product1 = createTestProduct(euros(12, 99), PRODUCT_QUANTITY_IN_STOCK);
    Product product2 = createTestProduct(euros(5, 97), PRODUCT_QUANTITY_IN_STOCK);

    cart.addProduct(product1, 3);
    cart.addProduct(product2, 5);

    softly.assertThat(cart.numberOfItems()).isEqualTo(8);
    softly.assertThat(cart.subTotal()).isEqualTo(euros(68, 82));
  }

  @Test
  void testAddProduct_addMoreItemsThanAvailableToTheCart_throwsException() {
    Cart cart = emptyCartForRandomCustomer();
    Product product = createTestProduct(euros(9, 97), PRODUCT_QUANTITY_IN_STOCK);

    ThrowingCallable invocation = () -> cart.addProduct(product, 200);

    assertThatExceptionOfType(InsufficientStockException.class)
        .isThrownBy(invocation)
        .satisfies(ex -> assertThat(ex.getItemsInStock()).isEqualTo(product.itemsInStock()));
  }

  @Test
  void testAddProduct_addAllAvailableItemsToTheCart_validResponse() {
    Cart cart = emptyCartForRandomCustomer();
    Product product = createTestProduct(euros(9, 97), PRODUCT_QUANTITY_IN_STOCK);

    ThrowingCallable invocation = () -> cart.addProduct(product, 3);

    assertThatNoException().isThrownBy(invocation);
  }

  @ParameterizedTest
  @ValueSource(ints = {-100, -1, 0})
  void testAddProduct_givenEmptyCart_addLessThanOneItemOfAProduct_throwsException(int quantity) {
    Cart cart = emptyCartForRandomCustomer();
    Product product = createTestProduct(euros(1, 49), PRODUCT_QUANTITY_IN_STOCK);

    ThrowingCallable invocation = () -> cart.addProduct(product, quantity);

    assertThatIllegalArgumentException().isThrownBy(invocation);
  }
}