package org.example.hahallohback.security.request;

import lombok.Data;

@Data
public class AuthRequest {
  private String username;
  private String password;
}

