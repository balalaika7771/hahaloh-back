package org.example.hahallohback.security.constans;

import java.util.List;


public class ExcludedPaths {

  public static final List<String> PATHS = List.of(
      "/api/doc-ui/**",
      "/api/auth/register",
      "/api/auth/login",
      "/api/oauth/*",
      "/v3/api-docs/**"
  );
}
