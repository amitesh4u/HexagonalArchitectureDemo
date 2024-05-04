package com.amitesh.shop.model.customer;

public record CustomerId(int id) {

  public CustomerId {
    if (id < 1) {
      throw new IllegalArgumentException("'id' must be a positive integer");
    }
  }
}
