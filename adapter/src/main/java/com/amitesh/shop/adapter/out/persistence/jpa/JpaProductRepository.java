package com.amitesh.shop.adapter.out.persistence.jpa;

import com.amitesh.shop.adapter.out.persistence.TestProducts;
import com.amitesh.shop.application.port.out.persistence.ProductRepository;
import com.amitesh.shop.model.product.Product;
import com.amitesh.shop.model.product.ProductId;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@ConditionalOnProperty(name = "persistence", havingValue = "mysql")
@Repository
public class JpaProductRepository implements ProductRepository {

  private final JpaProductSpringDataRepository springDataRepository;

  public JpaProductRepository(JpaProductSpringDataRepository springDataRepository) {
    this.springDataRepository = springDataRepository;
  }

  @PostConstruct
  private void createTestProducts() {
    TestProducts.TEST_PRODUCTS.forEach(this::save);
  }

  @Override
  @Transactional
  public void save(final Product product) {
    springDataRepository.save(ProductMapper.toJpaEntity(product));
  }

  @Override
  @Transactional
  public List<Product> findAll() {
    List<ProductJpaEntity> entities = springDataRepository.findAll();
    return ProductMapper.toModelEntities(entities);
  }

  @Override
  @Transactional
  public Optional<Product> findById(final ProductId productId) {
    Optional<ProductJpaEntity> productJpaEntity = springDataRepository.findById(productId.value());
    return productJpaEntity.map(ProductMapper::toModelEntity);
  }

  @Override
  @Transactional
  public List<Product> findByNameOrDescription(final String queryString) {
    List<ProductJpaEntity> entities = springDataRepository.findByNameOrDescriptionLike("%" + queryString + "%");
    return ProductMapper.toModelEntities(entities);
  }
}
