package com.amitesh.shop.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.amitesh.shop.application.port.out.persistence.ProductRepository;
import com.amitesh.shop.model.product.Product;
import com.amitesh.shop.model.product.ProductId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractProductRepositoryTest {

  @Autowired
  private ProductRepository productRepository;

  @Test
  void testFindById_givenATestProductId_returnsATestProduct() {
    ProductId productId = TestProducts.COMPUTER_MONITOR.id();

    Optional<Product> product = productRepository.findById(productId);

    assertThat(product).contains(TestProducts.COMPUTER_MONITOR);
  }

  @Test
  void testFindById_givenTheIdOfAProductNotPersisted_returnsAnEmptyOptional() {
    ProductId productId = new ProductId("00000");

    Optional<Product> product = productRepository.findById(productId);

    assertThat(product).isEmpty();
  }

  @Test
  void testFindByNameOrDescription_givenASearchQueryNotMatchingAnyProduct_returnsAnEmptyList() {
    String query = "not matching any product";

    List<Product> products = productRepository.findByNameOrDescription(query);

    assertThat(products).isEmpty();
  }

  @Test
  void testFindByNameOrDescription_givenASearchQueryMatchingOneProduct_returnsThatProduct() {
    String query = "lights";

    List<Product> products = productRepository.findByNameOrDescription(query);

    assertThat(products).containsExactlyInAnyOrder(TestProducts.LED_LIGHTS);
  }

  @Test
  void testFindByNameOrDescription_givenASearchQueryMatchingTwoProducts_returnsThoseProducts() {
    String query = "monitor";

    List<Product> products = productRepository.findByNameOrDescription(query);

    assertThat(products)
        .containsExactlyInAnyOrder(TestProducts.COMPUTER_MONITOR, TestProducts.MONITOR_DESK_MOUNT);
  }
}
