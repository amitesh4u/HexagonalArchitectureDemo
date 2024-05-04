package com.amitesh.shop.model.cart;

import com.amitesh.shop.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class CartLineItem {

  private final Product product;
  private int quantity;

}
