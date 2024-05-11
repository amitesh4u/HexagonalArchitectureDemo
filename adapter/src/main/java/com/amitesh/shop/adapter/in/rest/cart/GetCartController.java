package com.amitesh.shop.adapter.in.rest.cart;

import static com.amitesh.shop.adapter.in.rest.common.ControllerHelper.parseCustomerId;

import com.amitesh.shop.application.port.in.cart.GetCartUseCase;
import com.amitesh.shop.model.cart.Cart;
import com.amitesh.shop.model.customer.CustomerId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.CustomLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

@RestController
@RequestMapping("/carts")
@CustomLog
@ApiResponse(responseCode = "500", description = "Internal server error, this should not happen",
    content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = InternalServerError.class)))
@Tag(name = "Get Cart Controller", description = "Endpoint for getting Cart")
public class GetCartController {

  private final GetCartUseCase getCartUseCase;

  public GetCartController(GetCartUseCase getCartUseCase) {
    this.getCartUseCase = getCartUseCase;
  }

  @GetMapping("/{customerId}")
  @Operation(
      operationId = "GetCart",
      summary = "Get Cart",
      responses = @ApiResponse(responseCode = "200", description = "Updated Cart is returned",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartWebModel.class))))
  public CartWebModel getCart(@PathVariable("customerId") String customerIdString) {

    LOGGER.debug("Fetching cart for " + customerIdString);

    CustomerId customerId = parseCustomerId(customerIdString);
    Cart cart = getCartUseCase.getCart(customerId);
    CartWebModel cartWebModel = CartWebModel.fromDomainModel(cart);

    LOGGER.debug("Returning cart for {} is {}", customerIdString, cartWebModel);

    return cartWebModel;
  }

}
