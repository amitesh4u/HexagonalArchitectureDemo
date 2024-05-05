package com.amitesh.shop.application.port.in.cart;

import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.cart.OutOfStockException;
import com.amitesh.shop.model.customer.CustomerId;
import com.amitesh.shop.model.product.ProductId;

public interface AddToCartUseCase {

  Cart addToCart(CustomerId customerId, ProductId productId, int quantity)
      throws ProductNotFoundException, OutOfStockException;

}
