package org.example.hahallohback.core.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SimpleErrorResponse {
  private final String message;
  private final int code;
}
