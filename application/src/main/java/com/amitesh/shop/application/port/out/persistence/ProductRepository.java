package com.amitesh.shop.application.port.out.persistence;

import com.amitesh.shop.model.product.Product;
import com.amitesh.shop.model.product.ProductId;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

  void save(Product product);

  List<Product> findAll();

  Optional<Product> findById(ProductId productId);

  List<Product> findByNameOrDescription(String query);

}
