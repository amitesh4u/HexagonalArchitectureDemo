package com.amitesh.shop.adapter.out.persistence.jpa;

import com.amitesh.shop.application.port.out.persistence.CartRepository;
import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.customer.CustomerId;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@ConditionalOnProperty(name = "persistence", havingValue = "mysql")
@Repository
public class JpaCartRepository implements CartRepository {

  private final JpaCartSpringDataRepository springDataRepository;

  public JpaCartRepository(JpaCartSpringDataRepository springDataRepository) {
    this.springDataRepository = springDataRepository;
  }

  @Override
  @Transactional
  public void save(final Cart cart) {
    springDataRepository.save(CartMapper.toJpaEntity(cart));
  }

  @Override
  @Transactional
  public Optional<Cart> findByCustomerId(final CustomerId customerId) {
    Optional<CartJpaEntity> cartJpaEntity = springDataRepository.findById(customerId.value());
    return cartJpaEntity.map(CartMapper::toModelEntity);
  }

  @Override
  @Transactional
  public void deleteByCustomerId(final CustomerId customerId) {
    springDataRepository.deleteById(customerId.value());
  }
}
