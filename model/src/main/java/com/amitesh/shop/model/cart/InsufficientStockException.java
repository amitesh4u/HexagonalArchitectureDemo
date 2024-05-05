package com.amitesh.shop.model.cart;

import lombok.Getter;

@Getter
public class InsufficientStockException extends Exception {

  private final int itemsInStock;

  public InsufficientStockException(final String message, final int itemsInStock) {
    super(message);
    this.itemsInStock = itemsInStock;
  }

}
