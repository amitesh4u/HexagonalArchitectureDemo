package com.amitesh.shop.application.service.product;

import com.amitesh.shop.application.port.in.product.FindProductsUseCase;
import com.amitesh.shop.application.port.out.persistence.ProductRepository;
import com.amitesh.shop.model.product.Product;
import java.util.List;

public class FindProductsService implements FindProductsUseCase {

  private final ProductRepository productRepository;

  public FindProductsService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public List<Product> findByNameOrDescription(String query) {
    if (null == query || query.length() < 2) {
      throw new IllegalArgumentException("'query' must be at least two characters long");
    }

    return productRepository.findByNameOrDescription(query);
  }
}
