package base.controller.abstractions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;


public interface OAuthControllerInterface {

  /**
   * Перенаправление на страницу авторизации.
   */
  ResponseEntity<Void> authorize(HttpServletRequest request);

  /**
   * Обработка callback после авторизации.
   *
   * @param code  Код авторизации, переданный внешним сервисом.
   * @param state Временный ключ для проверки.
   * @return Подтверждение успешной привязки.
   */
  ResponseEntity<String> callback(@RequestParam String code, @RequestParam String state);
}
