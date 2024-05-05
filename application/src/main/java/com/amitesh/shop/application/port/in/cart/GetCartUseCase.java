package com.amitesh.shop.application.port.in.cart;

import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.customer.CustomerId;

public interface GetCartUseCase {

  Cart getCart(CustomerId customerId);
}
