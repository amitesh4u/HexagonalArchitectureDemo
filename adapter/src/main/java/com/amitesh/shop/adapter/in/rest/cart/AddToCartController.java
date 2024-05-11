package com.amitesh.shop.adapter.in.rest.cart;

import static com.amitesh.shop.adapter.in.rest.common.ControllerHelper.clientErrorException;
import static com.amitesh.shop.adapter.in.rest.common.ControllerHelper.parseCustomerId;
import static com.amitesh.shop.adapter.in.rest.common.ControllerHelper.parseProductId;

import com.amitesh.shop.adapter.in.rest.common.ClientErrorException;
import com.amitesh.shop.application.port.in.cart.AddToCartUseCase;
import com.amitesh.shop.application.port.in.cart.ProductNotFoundException;
import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.cart.InsufficientStockException;
import com.amitesh.shop.model.customer.CustomerId;
import com.amitesh.shop.model.product.ProductId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.CustomLog;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carts")
@CustomLog
@ApiResponse(responseCode = "400", description = "The requested product does not exist or stock not available",
    content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = ClientErrorException.class)))
/* Defined an empty @Content for Error. Only their descriptions will be displayed. */
@ApiResponse(responseCode = "500", description = "Internal server error. Please Try later", content = @Content)
@Tag(name = "Add to Cart Controller", description = "Endpoint for adding Products to Cart")
public class AddToCartController {

  private final AddToCartUseCase addToCartUseCase;

  public AddToCartController(AddToCartUseCase addToCartUseCase) {
    this.addToCartUseCase = addToCartUseCase;
  }

  @PostMapping("/{customerId}/line-items")
  @Operation(
      operationId = "AddToCart",
      summary = "Add Product to Cart",
      responses = @ApiResponse(responseCode = "200", description = "Updated Cart is returned",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartWebModel.class)),
          links = {@Link(operationRef = "GetCart", name = "Get Cart", operationId = "GetCart")}))
  public CartWebModel addLineItem(
      @PathVariable("customerId") String customerIdString,
      @RequestParam("productId") String productIdString,
      @RequestParam("quantity") int quantity) {
    CustomerId customerId = parseCustomerId(customerIdString);
    ProductId productId = parseProductId(productIdString);

    LOGGER.debug("Adding {} quantities of product {} to cart {}", quantity, productIdString,
        customerIdString);

    try {
      Cart cart = addToCartUseCase.addToCart(customerId, productId, quantity);
      CartWebModel cartWebModel = CartWebModel.fromDomainModel(cart);

      LOGGER.debug("Updated cart for {} is {}", customerIdString, cartWebModel);

      return cartWebModel;
    } catch (ProductNotFoundException e) {
      LOGGER.error("The requested product does not exist: " + productIdString);
      throw clientErrorException(
          HttpStatus.BAD_REQUEST, "The requested product does not exist");
    } catch (InsufficientStockException e) {
      String message = "Only %d items in stock".formatted(e.getItemsInStock());
      LOGGER.error(message + " for product " + productIdString);
      throw clientErrorException(HttpStatus.BAD_REQUEST, message);
    }
  }
}
