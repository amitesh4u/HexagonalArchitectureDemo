package com.amitesh.shop.adapter.in.rest.cart;

import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.price.Price;
import java.util.List;

public record CartWebModel(
    List<CartLineItemWebModel> lineItems, int numberOfItems, Price subTotal) {

  static CartWebModel fromDomainModel(final Cart cart) {
    return new CartWebModel(
        cart.lineItems().stream().map(CartLineItemWebModel::fromDomainModel).toList(),
        cart.numberOfItems(),
        cart.subTotal());
  }
}
