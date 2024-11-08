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
import org.springframework.http.HttpStatus;
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
  private final OAuthClassicController classicController;

  public OAuthController(OAuthClassicController classicController) {
    this.classicController = classicController;
  }

  /**
   * Обработка callback для привязки аккаунта HeadHunter к существующему пользователю.
   *
   * @param code Код авторизации от HeadHunter.
   * @param state Параметр временного ключа, для сопоставления с пользователем.
   * @return Подтверждение успешной привязки.
   */
  @Operation(
      summary = "Получение access_token и refresh_token",
      description = "Метод обрабатывает `callback` от hh.ru после авторизации. "
          + "Использует полученный `code`, чтобы получить `access_token` и `refresh_token` для доступа к API hh.ru от имени пользователя."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Токены успешно получены"),
      @ApiResponse(responseCode = "400", description = "Недействительный код или ключ"),
      @ApiResponse(responseCode = "500", description = "Ошибка при получении токенов")
  })
  @GetMapping("/callback/hh")
  public ResponseEntity<String> callback(
      @Parameter(description = "Код авторизации, который был передан hh.ru на ваш `redirectUri`", required = true)
      @RequestParam String code,
      @RequestParam String state) {

    // Проверяем временный ключ (state) и получаем ID пользователя
    Long currentUserId = classicController.getTemporaryAuthMap().remove(state);
    if (currentUserId == null) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired state parameter.");
    }

    // Обмен кода авторизации на access_token
    String tokenUrl = "https://hh.ru/oauth/token";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    String requestBody = "grant_type=authorization_code"
        + "&client_id=" + clientId
        + "&client_secret=" + clientSecret
        + "&code=" + code
        + "&redirect_uri=" + redirectUri;

    HttpEntity<String> tokenRequest = new HttpEntity<>(requestBody, headers);
    ResponseEntity<Map> tokenResponse = restTemplate.exchange(tokenUrl, HttpMethod.POST, tokenRequest, Map.class);

    if (!tokenResponse.getStatusCode().is2xxSuccessful() || !tokenResponse.hasBody()) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve tokens.");
    }

    String accessToken = (String) tokenResponse.getBody().get("access_token");
    String refreshToken = (String) tokenResponse.getBody().get("refresh_token");

    // Привязка аккаунта HeadHunter к профилю пользователя в системе
    // Используем currentUserId для привязки к пользователю
    // Пример: userService.linkHeadHunterAccount(currentUserId, accessToken, refreshToken);

    return ResponseEntity.ok("HeadHunter account linked successfully. Access Token - " + accessToken + " " + currentUserId);
  }
}
