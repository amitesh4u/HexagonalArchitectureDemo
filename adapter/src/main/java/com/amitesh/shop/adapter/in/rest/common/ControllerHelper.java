package com.amitesh.shop.adapter.in.rest.common;

import com.amitesh.shop.model.customer.CustomerId;
import com.amitesh.shop.model.product.ProductId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Common functionality for all REST controllers.
 */
public class ControllerHelper {

  private ControllerHelper() {
  }

  public static ClientErrorException clientErrorException(HttpStatus status, String message) {
    return new ClientErrorException(errorResponse(status, message));
  }

  public static ResponseEntity<ErrorEntity> errorResponse(HttpStatus status, String message) {
    ErrorEntity errorEntity = new ErrorEntity(status.value(), message);
    return ResponseEntity.status(status.value()).body(errorEntity);
  }

  public static CustomerId parseCustomerId(String string) {
    try {
      return new CustomerId(Integer.parseInt(string));
    } catch (IllegalArgumentException e) {
      throw clientErrorException(HttpStatus.BAD_REQUEST, "Invalid 'customerId'");
    }
  }

  public static ProductId parseProductId(String string) {
    if (string == null || string.isBlank()) {
      throw clientErrorException(HttpStatus.BAD_REQUEST, "Missing 'productId'");
    }

    try {
      return new ProductId(string);
    } catch (IllegalArgumentException e) {
      throw clientErrorException(HttpStatus.BAD_REQUEST, "Invalid 'productId'");
    }
  }
}
