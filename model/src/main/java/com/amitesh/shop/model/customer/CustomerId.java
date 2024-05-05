package com.amitesh.shop.model.customer;

public record CustomerId(int value) {

  public CustomerId {
    if (value < 1) {
      throw new IllegalArgumentException("'id' must be a positive integer");
    }
  }
}
