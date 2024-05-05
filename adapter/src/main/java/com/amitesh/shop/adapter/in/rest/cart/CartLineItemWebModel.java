package com.amitesh.shop.adapter.in.rest.cart;

import com.amitesh.shop.model.cart.CartLineItem;
import com.amitesh.shop.model.price.Price;
import com.amitesh.shop.model.product.Product;

public record CartLineItemWebModel(
    String productId, String productName, Price price, int quantity) {

  public static CartLineItemWebModel fromDomainModel(final CartLineItem lineItem) {
    Product product = lineItem.product();
    return new CartLineItemWebModel(
        product.id().value(), product.name(), product.price(), lineItem.quantity());
  }
}
