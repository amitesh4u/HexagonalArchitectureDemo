package com.amitesh.shop.bootstrap;

import com.amitesh.shop.adapter.in.rest.cart.AddToCartController;
import com.amitesh.shop.adapter.in.rest.cart.EmptyCartController;
import com.amitesh.shop.adapter.in.rest.cart.GetCartController;
import com.amitesh.shop.adapter.in.rest.product.FindProductsController;
import com.amitesh.shop.adapter.out.persistence.inmemory.InMemoryCartRepository;
import com.amitesh.shop.adapter.out.persistence.inmemory.InMemoryProductRepository;
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
import jakarta.ws.rs.core.Application;
import java.util.Set;

public class RestEasyUndertowShopApplication extends Application {

  private CartRepository cartRepository;
  private ProductRepository productRepository;

  /* Defining it manually in place of "automatic discovery of resources" for clarity */
  @SuppressWarnings("deprecation")
  @Override
  public Set<Object> getSingletons() {
    initPersistenceAdapters();
    return Set.of(
        addToCartController(),
        getCartController(),
        emptyCartController(),
        findProductsController());
  }

  private void initPersistenceAdapters() {
    cartRepository = new InMemoryCartRepository();
    productRepository = new InMemoryProductRepository();
  }

  private AddToCartController addToCartController() {
    AddToCartUseCase addToCartUseCase = new AddToCartService(cartRepository, productRepository);
    return new AddToCartController(addToCartUseCase);
  }

  private GetCartController getCartController() {
    GetCartUseCase getCartUseCase = new GetCartService(cartRepository);
    return new GetCartController(getCartUseCase);
  }

  private EmptyCartController emptyCartController() {
    EmptyCartUseCase emptyCartUseCase = new EmptyCartService(cartRepository);
    return new EmptyCartController(emptyCartUseCase);
  }

  private FindProductsController findProductsController() {
    FindProductsUseCase findProductsUseCase = new FindProductsService(productRepository);
    return new FindProductsController(findProductsUseCase);
  }
}