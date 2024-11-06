package org.example.hahallohback.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public class InvalidCredentialsException extends RuntimeException {

  private final int errorCode;

  public InvalidCredentialsException(String message, int errorCode) {
    super(message);
    this.errorCode = errorCode;
  }
}
