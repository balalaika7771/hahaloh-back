package base.util;

import base.model.JwtAuthentication;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;


/**
 * @author Ivan Zhendorenko
 */
@NoArgsConstructor(access = PRIVATE)
public class Auth {

  /**
   * Аутентификация в виде токена для заголовка
   *
   * @return Аутентификация
   */
  public static String getAsBearer() {
    return "Bearer " + getAuth().getToken();
  }

  /**
   * Получить аутентификацию пользователя. Работает только если вход был через контроллер
   *
   * @return Аутентификация
   */
  public static JwtAuthentication getAuth() {
    return (JwtAuthentication) getContext().getAuthentication();
  }
}
