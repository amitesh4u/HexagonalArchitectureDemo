package com.amitesh.shop.model;

import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.customer.CustomerId;
import com.amitesh.shop.model.price.Price;
import com.amitesh.shop.model.product.Product;
import com.amitesh.shop.model.product.ProductId;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class ModelTestHelper {

  public static final int PRODUCT_ID_LENGTH = 8;
  public static final String DUMMY_PRODUCT_NAME = "Test Product";
  public static final String DUMMY_PRODUCT_DESC = "Test Product Description";
  public static final int PRODUCT_QUANTITY_IN_STOCK = 100;

  public static final Currency EUR = Currency.getInstance("EUR");
  public static final Currency USD = Currency.getInstance("USD");

  public static final Product RANDOM_PRODUCT = createTestProduct(euros(11, 0), PRODUCT_QUANTITY_IN_STOCK);


  public static Cart emptyCartForRandomCustomer() {
    return new Cart(new CustomerId(ThreadLocalRandom.current().nextInt(1_000_000)));
  }

  public static ProductId randomProductId() {
    String randomId = UUID.randomUUID().toString();
    return new ProductId(randomId.substring(randomId.length() - PRODUCT_ID_LENGTH));
  }

  public static Product createTestProduct(Price price, int quantity) {
    return new Product(randomProductId(), DUMMY_PRODUCT_NAME, DUMMY_PRODUCT_DESC, price, quantity);
  }


  public static Price euros(final int euros, final int cents) {
    return priceOf(EUR, euros, cents);
  }

  public static Price usDollars(final int dollars, final int cents) {
    return priceOf(USD, dollars, cents);
  }

  public static Price priceOf(final Currency currency, final int major, final int minor) {
    int scale = currency.getDefaultFractionDigits();
    return new Price(currency, BigDecimal.valueOf(major).add(BigDecimal.valueOf(minor, scale)));
  }
}
