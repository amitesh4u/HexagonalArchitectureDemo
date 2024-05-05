package com.amitesh.shop.model.product;

public record ProductId(String id) {

  public ProductId {
    if (null == id || id.isBlank()) {
      throw new IllegalArgumentException("'id' must not be null or empty");
    }
  }
}
