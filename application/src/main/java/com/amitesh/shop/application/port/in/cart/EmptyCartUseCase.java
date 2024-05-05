package com.amitesh.shop.application.port.in.cart;

import com.amitesh.shop.model.customer.CustomerId;

public interface EmptyCartUseCase {

  void emptyCart(CustomerId customerId);
}
