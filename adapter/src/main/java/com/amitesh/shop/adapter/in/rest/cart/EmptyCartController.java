package com.amitesh.shop.adapter.in.rest.cart;

import static com.amitesh.shop.adapter.in.rest.common.ControllerHelper.parseCustomerId;

import com.amitesh.shop.application.port.in.cart.EmptyCartUseCase;
import com.amitesh.shop.model.customer.CustomerId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.CustomLog;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carts")
@CustomLog
@ApiResponse(responseCode = "500", description = "Internal server error. Please Try later", content = @Content)
@Tag(name = "Empty Cart Controller", description = "Endpoint for emptying Cart")
public class EmptyCartController {

  private final EmptyCartUseCase emptyCartUseCase;

  public EmptyCartController(EmptyCartUseCase emptyCartUseCase) {
    this.emptyCartUseCase = emptyCartUseCase;
  }

  @DeleteMapping("/{customerId}")
  @Operation(
      operationId = "DeleteCart",
      summary = "Delete Cart",
      responses = @ApiResponse(responseCode = "200",
          description = "The Cart has been emptied",
          content = @Content(mediaType = "application/json")))
  public void deleteCart(@PathVariable("customerId") String customerIdString) {
    LOGGER.debug("Deleting cart for " + customerIdString);
    CustomerId customerId = parseCustomerId(customerIdString);
    emptyCartUseCase.emptyCart(customerId);
  }

}
