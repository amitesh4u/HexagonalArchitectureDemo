package com.amitesh.shop.adapter.in.rest.cart;

import static com.amitesh.shop.adapter.in.rest.common.ControllerHelper.clientErrorException;
import static com.amitesh.shop.adapter.in.rest.common.ControllerHelper.parseCustomerId;
import static com.amitesh.shop.adapter.in.rest.common.ControllerHelper.parseProductId;

import com.amitesh.shop.application.port.in.cart.AddToCartUseCase;
import com.amitesh.shop.application.port.in.cart.ProductNotFoundException;
import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.cart.InsufficientStockException;
import com.amitesh.shop.model.customer.CustomerId;
import com.amitesh.shop.model.product.ProductId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carts")
public class AddToCartController {

  private final AddToCartUseCase addToCartUseCase;

  public AddToCartController(AddToCartUseCase addToCartUseCase) {
    this.addToCartUseCase = addToCartUseCase;
  }

  @PostMapping("/{customerId}/line-items")
  public CartWebModel addLineItem(
      @PathVariable("customerId") String customerIdString,
      @RequestParam("productId") String productIdString,
      @RequestParam("quantity") int quantity) {
    CustomerId customerId = parseCustomerId(customerIdString);
    ProductId productId = parseProductId(productIdString);

    try {
      Cart cart = addToCartUseCase.addToCart(customerId, productId, quantity);
      return CartWebModel.fromDomainModel(cart);
    } catch (ProductNotFoundException e) {
      throw clientErrorException(
          HttpStatus.BAD_REQUEST, "The requested product does not exist");
    } catch (InsufficientStockException e) {
      throw clientErrorException(
          HttpStatus.BAD_REQUEST,
          "Only %d items in stock".formatted(e.getItemsInStock()));
    }
  }
}
