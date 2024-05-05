package com.amitesh.shop.adapter.in.rest.product;

import com.amitesh.shop.model.price.Price;
import com.amitesh.shop.model.product.Product;

public record ProductInListWebModel(String id, String name, Price price, int itemsInStock) {

  public static ProductInListWebModel fromDomainModel(final Product product) {
    return new ProductInListWebModel(
        product.id().value(), product.name(), product.price(), product.itemsInStock());
  }
}
