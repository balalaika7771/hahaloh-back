package base.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;


/**
 * Исключение для проброса ошибок валидации
 *
 * @author Ivan Zhendorenko
 */
@RequiredArgsConstructor
public class SimpleValidationException extends RuntimeException {

  /**
   * Мапа вида: путь до свойства - сообщение об ошибке
   */
  private final Map<String, String> errors;

  /**
   * Получить тело для ответа
   *
   * @return Тело
   */
  public Map<String, Object> getErrorsForResponse() {
    var response = new HashMap<String, Object>();

    response.put("errors", errors);

    return response;
  }
}
