package com.amitesh.shop.model.product;

import java.util.Objects;

public record ProductId(String id) {

  public ProductId {
    Objects.requireNonNull(id, "'id' must not be null");
    if (id.isBlank()) {
      throw new IllegalArgumentException("'id' must not be empty");
    }
  }
}
