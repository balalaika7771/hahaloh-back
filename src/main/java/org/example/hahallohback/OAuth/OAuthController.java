package org.example.hahallohback.OAuth;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;


@Controller
@RequestMapping("/api/oauth")
public class OAuthController {

  @Value("${hh.client-id}")
  private String clientId;

  @Value("${hh.client-secret}")
  private String clientSecret;

  @Value("${hh.redirect-uri}")
  private String redirectUri;

  private final RestTemplate restTemplate = new RestTemplate();

  @GetMapping("/authorize/hh")
  public String authorize() {
    String authUrl = "https://hh.ru/oauth/authorize"
        + "?response_type=code&client_id=" + clientId
        + "&redirect_uri=" + redirectUri;
    return "redirect:" + authUrl;
  }

  @GetMapping("/callback/hh")
  public ResponseEntity<String> callback(@RequestParam String code) {
    // Обмен кода авторизации на токен
    String tokenUrl = "https://hh.ru/oauth/token";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<String> request = new HttpEntity<>(
        "grant_type=authorization_code"
            + "&client_id=" + clientId
            + "&client_secret=" + clientSecret
            + "&code=" + code
            + "&redirect_uri=" + redirectUri,
        headers
    );

    ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

    // Здесь можно сохранить токены (access_token и refresh_token) в базу данных
    String accessToken = (String) response.getBody().get("access_token");
    String refreshToken = (String) response.getBody().get("refresh_token");

    return ResponseEntity.ok("Tokens received: Access Token - " + accessToken);
  }
}
