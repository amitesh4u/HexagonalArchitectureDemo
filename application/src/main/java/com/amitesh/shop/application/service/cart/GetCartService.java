package com.amitesh.shop.application.service.cart;

import com.amitesh.shop.application.port.in.cart.GetCartUseCase;
import com.amitesh.shop.application.port.out.persistence.CartRepository;
import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.customer.CustomerId;

public class GetCartService implements GetCartUseCase {

  private final CartRepository cartRepository;

  public GetCartService(CartRepository cartRepository) {
    this.cartRepository = cartRepository;
  }

  @Override
  public Cart getCart(CustomerId customerId) {
    if (null == customerId) {
      throw new IllegalArgumentException("'customerId' must not be null");
    }
    return cartRepository
        .findByCustomerId(customerId)
        .orElseGet(() -> new Cart(customerId));
  }
}
