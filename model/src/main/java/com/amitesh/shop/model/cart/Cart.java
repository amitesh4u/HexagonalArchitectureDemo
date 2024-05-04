package com.amitesh.shop.model.cart;

import com.amitesh.shop.model.customer.CustomerId;
import com.amitesh.shop.model.product.ProductId;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@RequiredArgsConstructor
public class Cart {

  @Getter
  private final CustomerId id; // cart ID = customer ID

  private final Map<ProductId, CartLineItem> lineItems = new LinkedHashMap<>();

}
