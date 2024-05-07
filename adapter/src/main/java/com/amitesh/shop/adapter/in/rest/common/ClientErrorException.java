package com.amitesh.shop.adapter.in.rest.common;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class ClientErrorException extends RuntimeException {

  private final ResponseEntity<ErrorEntity> response;

  public ClientErrorException(ResponseEntity<ErrorEntity> response) {
    super(getMessage(response));
    this.response = response;
  }

  private static String getMessage(ResponseEntity<ErrorEntity> response) {
    ErrorEntity body = response.getBody();
    return body != null ? body.errorMessage() : null;
  }
}
