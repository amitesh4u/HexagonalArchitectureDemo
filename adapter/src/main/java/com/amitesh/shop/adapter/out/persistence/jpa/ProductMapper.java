package com.amitesh.shop.adapter.out.persistence.jpa;

import com.amitesh.shop.model.price.Price;
import com.amitesh.shop.model.product.Product;
import com.amitesh.shop.model.product.ProductId;
import java.util.Currency;
import java.util.List;

final class ProductMapper {

  static ProductJpaEntity toJpaEntity(Product product) {
    ProductJpaEntity jpaEntity = new ProductJpaEntity();

    jpaEntity.setId(product.id().value());
    jpaEntity.setName(product.name());
    jpaEntity.setDescription(product.description());
    jpaEntity.setPriceCurrency(product.price().currency().getCurrencyCode());
    jpaEntity.setPriceAmount(product.price().amount());
    jpaEntity.setItemsInStock(product.itemsInStock());

    return jpaEntity;
  }

  static Product toModelEntity(ProductJpaEntity jpaEntity) {
    return new Product(
        new ProductId(jpaEntity.getId()),
        jpaEntity.getName(),
        jpaEntity.getDescription(),
        new Price(Currency.getInstance(jpaEntity.getPriceCurrency()), jpaEntity.getPriceAmount()),
        jpaEntity.getItemsInStock());
  }

  static List<Product> toModelEntities(List<ProductJpaEntity> jpaEntities) {
    return jpaEntities.stream().map(ProductMapper::toModelEntity).toList();
  }
}
