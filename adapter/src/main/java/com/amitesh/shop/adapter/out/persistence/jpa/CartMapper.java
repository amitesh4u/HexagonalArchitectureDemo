package com.amitesh.shop.adapter.out.persistence.jpa;

import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.cart.CartLineItem;
import com.amitesh.shop.model.customer.CustomerId;
import java.util.Optional;

final class CartMapper {

  private CartMapper() {}

  static CartJpaEntity toJpaEntity(Cart cart) {
    CartJpaEntity cartJpaEntity = new CartJpaEntity();
    cartJpaEntity.setCustomerId(cart.id().value());

    cartJpaEntity.setLineItems(
        cart.lineItems().stream().map(lineItem -> toJpaEntity(cartJpaEntity, lineItem)).toList());

    return cartJpaEntity;
  }

  static CartLineItemJpaEntity toJpaEntity(CartJpaEntity cartJpaEntity, CartLineItem lineItem) {
    ProductJpaEntity productJpaEntity = new ProductJpaEntity();
    productJpaEntity.setId(lineItem.product().id().value());

    CartLineItemJpaEntity entity = new CartLineItemJpaEntity();
    entity.setCart(cartJpaEntity);
    entity.setProduct(productJpaEntity);
    entity.setQuantity(lineItem.quantity());

    return entity;
  }

  static Optional<Cart> toModelEntityOptional(CartJpaEntity cartJpaEntity) {
    if (cartJpaEntity == null) {
      return Optional.empty();
    }

    CustomerId customerId = new CustomerId(cartJpaEntity.getCustomerId());
    Cart cart = new Cart(customerId);

    for (CartLineItemJpaEntity lineItemJpaEntity : cartJpaEntity.getLineItems()) {
      cart.recreateCart(
          ProductMapper.toModelEntity(lineItemJpaEntity.getProduct()),
          lineItemJpaEntity.getQuantity());
    }

    return Optional.of(cart);
  }

}
