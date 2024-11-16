package base.controller.abstractions;

import base.abstractions.Identifiable;
import base.service.abstractions.OAuthService;
import base.util.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Objects;
import org.example.hahallohback.core.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


public interface OAuthController {

  OAuthService svc();


  /**
   * Перенаправление на страницу авторизации внешнего сервиса.
   */
  @Operation(summary = "Перенаправление на страницу авторизации")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "302", description = "Пользователь перенаправлен на страницу авторизации"),
      @ApiResponse(responseCode = "500", description = "Ошибка при генерации URL для авторизации")
  })
  @GetMapping("/authorize")
  default ResponseEntity<Void> authorize(HttpServletRequest request) {
    Long currentUserId = getCurrentUserId();
    if (currentUserId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(svc().registerStateAndGetAuthUrl(currentUserId)));
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

  /**
   * Обработка callback для привязки аккаунта после авторизации.
   */

  @Operation(summary = "Получение access_token и refresh_token")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Токены успешно получены"),
      @ApiResponse(responseCode = "400", description = "Недействительный код или ключ"),
      @ApiResponse(responseCode = "500", description = "Ошибка при получении токенов")
  })
  @GetMapping("/callback")
  default ResponseEntity<String> callback(
      @Parameter(description = "Код авторизации", required = true)
      @RequestParam String code,
      @RequestParam String state) {

    svc().saveToken(code, state);

    // Перенаправление в личный кабинет
    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/api/doc-ui/")).build();
  }

  private Long getCurrentUserId() {
    return ((Identifiable<User>) (Objects.requireNonNull(Auth.getCurrentUser()))).getId();
  }
}
