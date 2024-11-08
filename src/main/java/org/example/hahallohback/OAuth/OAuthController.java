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

  /**
   * Метод перенаправляет пользователя на страницу авторизации HeadHunter.
   * Пользователь должен разрешить доступ, после чего он будет перенаправлен
   * обратно на указанный `redirectUri` с параметром `code`.
   *
   * @return URL для перенаправления на страницу авторизации hh.ru.
   */
  @Operation(
      summary = "Перенаправление на страницу авторизации HeadHunter",
      description = "Этот метод направляет пользователя на страницу hh.ru для авторизации. "
          + "Пользователь предоставляет разрешение, и HeadHunter перенаправляет его обратно на ваш `redirectUri` с параметром `code`, который нужен для получения токенов."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "302", description = "Пользователь перенаправлен на страницу авторизации HeadHunter"),
      @ApiResponse(responseCode = "500", description = "Ошибка при генерации URL для авторизации")
  })
  @GetMapping("/authorize/hh")
  public String authorize() {
    String authUrl = "https://hh.ru/oauth/authorize"
        + "?response_type=code&client_id=" + clientId
        + "&redirect_uri=" + redirectUri;
    return "redirect:" + authUrl;
  }

  /**
   * Метод обрабатывает редирект от HeadHunter и получает `access_token` и `refresh_token`.
   * Использует код авторизации (`code`), чтобы обменять его на токены через API hh.ru.
   *
   * @param code Временный код авторизации, полученный от HeadHunter (параметр запроса).
   * @return Ответ с `access_token` и `refresh_token`.
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

    // Формируем URL для обмена кода авторизации на токен
    String tokenUrl = "https://hh.ru/oauth/token";

    // Заголовки запроса для POST-запроса на получение токенов
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    // Тело запроса с параметрами
    HttpEntity<String> request = new HttpEntity<>(
        "grant_type=authorization_code"
            + "&client_id=" + clientId
            + "&client_secret=" + clientSecret
            + "&code=" + code
            + "&redirect_uri=" + redirectUri,
        headers
    );

    // Выполняем запрос и получаем токены
    ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

    // Извлекаем токены из ответа
    String accessToken = (String) response.getBody().get("access_token");
    String refreshToken = (String) response.getBody().get("refresh_token");

    // Возвращаем токены в ответе
    return ResponseEntity.ok("Tokens received: Access Token - " + accessToken);
  }
}

