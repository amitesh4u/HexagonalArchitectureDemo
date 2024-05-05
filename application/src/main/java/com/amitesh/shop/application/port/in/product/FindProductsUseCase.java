package com.amitesh.shop.application.port.in.product;

import com.amitesh.shop.model.product.Product;
import java.util.List;

public interface FindProductsUseCase {

  /* Query must be at least 2 characters long */
  List<Product> findByNameOrDescription(String query);
}
