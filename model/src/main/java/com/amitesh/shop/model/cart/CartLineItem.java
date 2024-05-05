package com.amitesh.shop.model.cart;

import com.amitesh.shop.model.price.Price;
import com.amitesh.shop.model.product.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class CartLineItem {

  private final Product product;
  private int quantity;

  public void increaseQuantityBy(final int augend, final int itemsInStock)
      throws OutOfStockException {
    if (augend < 1) {
      throw new IllegalArgumentException("You must add at least one item");
    }

    int newQuantity = quantity + augend;
    if (itemsInStock < newQuantity) {
      throw new OutOfStockException(
          "Product %s has less items in stock (%d) than the requested total quantity (%d)"
              .formatted(product.id(), product.itemsInStock(), newQuantity),
          product.itemsInStock());
    }

    this.quantity = newQuantity;
  }

  public Price subTotal() {
    return product.price().multiply(quantity);
  }
}
