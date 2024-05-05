package com.amitesh.shop.adapter.in.rest.common;

import com.amitesh.shop.model.customer.CustomerId;
import com.amitesh.shop.model.product.ProductId;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;

/**
 * Common functionality for all REST controllers.
 */
public class ControllerHelper {

  private ControllerHelper() {
  }

  public static ClientErrorException clientErrorException(Response.Status status, String message) {
    return new ClientErrorException(errorResponse(status, message));
  }

  public static Response errorResponse(Response.Status status, String message) {
    ErrorEntity errorEntity = new ErrorEntity(status.getStatusCode(), message);
    return Response.status(status).entity(errorEntity).build();
  }

  public static CustomerId parseCustomerId(String string) {
    try {
      return new CustomerId(Integer.parseInt(string));
    } catch (IllegalArgumentException e) {
      throw clientErrorException(Response.Status.BAD_REQUEST, "Invalid 'customerId'");
    }
  }

  public static ProductId parseProductId(String string) {
    if (string == null || string.isBlank()) {
      throw clientErrorException(Response.Status.BAD_REQUEST, "Missing 'productId'");
    }

    try {
      return new ProductId(string);
    } catch (IllegalArgumentException e) {
      throw clientErrorException(Response.Status.BAD_REQUEST, "Invalid 'productId'");
    }
  }
}
