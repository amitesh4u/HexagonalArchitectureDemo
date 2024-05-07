package com.amitesh.shop;

import com.amitesh.shop.application.port.in.cart.AddToCartUseCase;
import com.amitesh.shop.application.port.in.cart.EmptyCartUseCase;
import com.amitesh.shop.application.port.in.cart.GetCartUseCase;
import com.amitesh.shop.application.port.in.product.FindProductsUseCase;
import com.amitesh.shop.application.port.out.persistence.CartRepository;
import com.amitesh.shop.application.port.out.persistence.ProductRepository;
import com.amitesh.shop.application.service.cart.AddToCartService;
import com.amitesh.shop.application.service.cart.EmptyCartService;
import com.amitesh.shop.application.service.cart.GetCartService;
import com.amitesh.shop.application.service.product.FindProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Spring application configuration, making beans from services defined in application module.
 */
@SpringBootApplication
public class SpringAppConfig {

  private final CartRepository cartRepository;

  private final ProductRepository productRepository;

  @Autowired
  public SpringAppConfig(CartRepository cartRepository, ProductRepository productRepository) {
    this.cartRepository = cartRepository;
    this.productRepository = productRepository;
  }

  @Bean
  GetCartUseCase getCartUseCase() {
    return new GetCartService(cartRepository);
  }

  @Bean
  EmptyCartUseCase emptyCartUseCase() {
    return new EmptyCartService(cartRepository);
  }

  @Bean
  FindProductsUseCase findProductsUseCase() {
    return new FindProductsService(productRepository);
  }

  @Bean
  AddToCartUseCase addToCartUseCase() {
    return new AddToCartService(cartRepository, productRepository);
  }
}
