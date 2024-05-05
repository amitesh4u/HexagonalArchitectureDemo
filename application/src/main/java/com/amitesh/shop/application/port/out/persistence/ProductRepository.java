package com.amitesh.shop.application.port.out.persistence;

import com.amitesh.shop.model.product.Product;
import java.util.List;

public interface ProductRepository {

  List<Product> findByNameOrDescription(String query);
}
