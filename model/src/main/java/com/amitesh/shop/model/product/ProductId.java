package com.amitesh.shop.model.product;

public record ProductId(String value) {

  public ProductId {
    if (null == value || value.isBlank()) {
      throw new IllegalArgumentException("'id' must not be null or empty");
    }
  }
}
