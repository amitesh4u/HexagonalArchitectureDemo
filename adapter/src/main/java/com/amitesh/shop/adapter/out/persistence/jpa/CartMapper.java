package com.amitesh.shop.adapter.out.persistence.jpa;

import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.cart.CartLineItem;
import com.amitesh.shop.model.customer.CustomerId;

final class CartMapper {

  private CartMapper() {
  }

  static CartJpaEntity toJpaEntity(final Cart cart) {
    CartJpaEntity cartJpaEntity = new CartJpaEntity();
    cartJpaEntity.setCustomerId(cart.id().value());

    cartJpaEntity.setLineItems(
        cart.lineItems().stream().map(lineItem -> toJpaEntity(cartJpaEntity, lineItem)).toList());

    return cartJpaEntity;
  }

  static CartLineItemJpaEntity toJpaEntity(final CartJpaEntity cartJpaEntity, final CartLineItem lineItem) {
    ProductJpaEntity productJpaEntity = new ProductJpaEntity();
    productJpaEntity.setId(lineItem.product().id().value());

    CartLineItemJpaEntity entity = new CartLineItemJpaEntity();
    entity.setCart(cartJpaEntity);
    entity.setProduct(productJpaEntity);
    entity.setQuantity(lineItem.quantity());

    return entity;
  }

  static Cart toModelEntity(final CartJpaEntity cartJpaEntity) {
    CustomerId customerId = new CustomerId(cartJpaEntity.getCustomerId());
    Cart cart = new Cart(customerId);

    for (CartLineItemJpaEntity lineItemJpaEntity : cartJpaEntity.getLineItems()) {
      cart.recreateCart(
          ProductMapper.toModelEntity(lineItemJpaEntity.getProduct()),
          lineItemJpaEntity.getQuantity());
    }

    return cart;
  }
}
