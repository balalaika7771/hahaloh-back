package org.example.hahallohback.OAuth.controller;

import base.util.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.hahallohback.OAuth.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {

  @Value("${hh.client-id}")
  private String clientId;

  @Value("${hh.redirect-uri}")
  private String redirectUri;

  private final TokenService tokenService;

  /**
   * Перенаправление на страницу авторизации HeadHunter.
   * Пользователь должен предоставить разрешение, и HeadHunter затем перенаправит его
   * обратно на указанный redirectUri с параметром `code`.
   */
  @Operation(summary = "Перенаправление на страницу авторизации HeadHunter")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "302", description = "Пользователь перенаправлен на страницу авторизации HeadHunter"),
      @ApiResponse(responseCode = "500", description = "Ошибка при генерации URL для авторизации")
  })
  @GetMapping("/authorize/hh")
  public ResponseEntity<Void> authorize(HttpServletRequest request) {
    Long currentUserId = getCurrentUserId();
    if (currentUserId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String state = UUID.randomUUID().toString();
    tokenService.storeState(currentUserId, state);

    // Формируем URL для авторизации
    String authUrl = "https://hh.ru/oauth/authorize"
        + "?response_type=code&client_id=" + clientId
        + "&redirect_uri=" + redirectUri
        + "&state=" + state;

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(authUrl));
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

  /**
   * Обработка callback для привязки аккаунта HeadHunter к существующему пользователю.
   *
   * @param code Код авторизации от HeadHunter.
   * @param state Параметр временного ключа для проверки.
   * @return Подтверждение успешной привязки.
   */
  @Operation(summary = "Получение access_token и refresh_token")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Токены успешно получены"),
      @ApiResponse(responseCode = "400", description = "Недействительный код или ключ"),
      @ApiResponse(responseCode = "500", description = "Ошибка при получении токенов")
  })
  @GetMapping("/callback/hh")
  public ResponseEntity<Void> callback(
      @Parameter(description = "Код авторизации от hh.ru", required = true) @RequestParam String code,
      @Parameter(description = "State для проверки подлинности", required = true) @RequestParam String state) {

    Long currentUserId = tokenService.retrieveUserIdByState(state);
    if (currentUserId == null) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Обмен кода авторизации на токены
    boolean success = tokenService.exchangeCodeForTokens(currentUserId, code);
    if (!success) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // Перенаправление в личный кабинет
    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/user/dashboard")).build();
  }

  // Метод для получения ID текущего пользователя из сессии или токена
  private Long getCurrentUserId() {
    return Objects.requireNonNull(Auth.getCurrentUser()).getId();
  }
}

