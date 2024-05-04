package com.amitesh.shop.model.cart;

public class OutOfStockException extends Exception {

  private final int itemsInStock;

  public OutOfStockException(final String message, final int itemsInStock) {
    super(message);
    this.itemsInStock = itemsInStock;
  }

  public int itemsInStock() {
    return itemsInStock;
  }

}
