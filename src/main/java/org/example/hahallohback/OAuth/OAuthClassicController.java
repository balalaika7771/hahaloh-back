package org.example.hahallohback.OAuth;

import base.util.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/oauth")
public class OAuthClassicController {

  @Value("${hh.client-id}")
  private String clientId;

  @Value("${hh.redirect-uri}")
  private String redirectUri;

  // Геттер для карты временных ключей, чтобы контроллер callback мог её использовать
  // Временное хранилище для сопоставления ключа с ID пользователя
  @Getter
  private final Map<String, Long> temporaryAuthMap = new HashMap<>();

  /**
   * Перенаправление на страницу авторизации HeadHunter.
   * Пользователь должен предоставить разрешение, и HeadHunter затем перенаправит его
   * обратно на указанный redirectUri с параметром `code`.
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
  public ResponseEntity<Void> authorize(HttpServletRequest request) {
    // Получаем ID текущего пользователя из сессии или токена
    Long currentUserId = getCurrentUserId();  // Реализуйте получение ID пользователя
    if (currentUserId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Создаем временный ключ и сохраняем его с ID пользователя
    String tempKey = UUID.randomUUID().toString();
    temporaryAuthMap.put(tempKey, currentUserId);

    // Формируем URL для авторизации с параметром `state`, который включает временный ключ
    String authUrl = "https://hh.ru/oauth/authorize"
        + "?response_type=code&client_id=" + clientId
        + "&redirect_uri=" + redirectUri
        + "&state=" + tempKey;

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(authUrl));
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }


  private Long getCurrentUserId() {
    return Objects.requireNonNull(Auth.getCurrentUser()).getId();
  }


}


