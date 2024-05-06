package com.amitesh.shop.application.service.cart;

import com.amitesh.shop.application.port.in.cart.EmptyCartUseCase;
import com.amitesh.shop.application.port.out.persistence.CartRepository;
import com.amitesh.shop.model.customer.CustomerId;

public class EmptyCartService implements EmptyCartUseCase {

  private final CartRepository cartRepository;

  public EmptyCartService(CartRepository cartRepository) {
    this.cartRepository = cartRepository;
  }

  @Override
  public void emptyCart(CustomerId customerId) {
    if (null == customerId) {
      throw new IllegalArgumentException("'customerId' must not be null");
    }
    cartRepository.deleteByCustomerId(customerId);
  }
}
