package org.example.hahallohback.OAuth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/oauth")
public class OAuthController {

  @Value("${hh.client-id}")
  private String clientId;

  @Value("${hh.client-secret}")
  private String clientSecret;

  @Value("${hh.redirect-uri}")
  private String redirectUri;

  private final RestTemplate restTemplate = new RestTemplate();

  /**
   * Обработка обратного вызова для обмена кода авторизации на токены.
   * Использует полученный `code`, чтобы запросить `access_token` и `refresh_token`.
   *
   * @param code Код авторизации, полученный от HeadHunter.
   * @return Токены доступа и обновления.
   */
  @Operation(
      summary = "Получение access_token и refresh_token",
      description = "Метод обрабатывает `callback` от hh.ru после авторизации. "
          + "Использует полученный `code`, чтобы получить `access_token` и `refresh_token` для доступа к API hh.ru от имени пользователя."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Токены успешно получены"),
      @ApiResponse(responseCode = "400", description = "Неверный код авторизации"),
      @ApiResponse(responseCode = "500", description = "Ошибка при получении токенов")
  })
  @GetMapping("/callback/hh")
  public ResponseEntity<String> callback(
      @Parameter(description = "Код авторизации, который был передан hh.ru на ваш `redirectUri`", required = true)
      @RequestParam String code) {

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

    String accessToken = (String) response.getBody().get("access_token");
    String refreshToken = (String) response.getBody().get("refresh_token");

    return ResponseEntity.ok("Tokens received: Access Token - " + accessToken);
  }
}
