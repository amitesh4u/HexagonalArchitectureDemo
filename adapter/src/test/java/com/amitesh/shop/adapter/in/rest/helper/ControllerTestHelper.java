package com.amitesh.shop.adapter.in.rest.helper;

import static com.amitesh.shop.model.ModelTestHelper.PRODUCT_QUANTITY_IN_STOCK;
import static com.amitesh.shop.model.ModelTestHelper.createTestProduct;
import static com.amitesh.shop.model.ModelTestHelper.usDollars;

import com.amitesh.shop.model.customer.CustomerId;
import com.amitesh.shop.model.product.Product;

public final class ControllerTestHelper {

  public static final CustomerId TEST_CUSTOMER_ID = new CustomerId(61157);
  public static final Product TEST_PRODUCT_1 = createTestProduct(usDollars(19, 99), PRODUCT_QUANTITY_IN_STOCK);
  public static final Product TEST_PRODUCT_2 = createTestProduct(usDollars(25, 99), PRODUCT_QUANTITY_IN_STOCK);
}
