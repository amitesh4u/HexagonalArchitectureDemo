package com.amitesh.shop.adapter.in.rest.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The Spring framework now intercepts a {@link ClientErrorException} using the ClientErrorHandler,
 * and the REST controller returns the error code and the error message stored in the exception.
 */
@RestControllerAdvice
public class ClientErrorHandler {

  @ExceptionHandler(ClientErrorException.class)
  public ResponseEntity<ErrorEntity> handleProductNotFoundException(ClientErrorException ex) {
    return ex.getResponse();
  }
}
