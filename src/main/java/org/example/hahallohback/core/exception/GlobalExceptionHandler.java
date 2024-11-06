package org.example.hahallohback.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<SimpleErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
    SimpleErrorResponse errorResponse = new SimpleErrorResponse(ex.getMessage(), ex.getErrorCode());
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }
}
