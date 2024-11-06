package base.exception;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;


/**
 * @author Ivan Zhendorenko
 */
@RequiredArgsConstructor
public class ToasterException extends RuntimeException {

  /**
   * Список ошибок
   */
  private final List<String> errors;

  public Map<String, Object> getErrorsForResponse() {
    return Map.of("toastErrors", errors);
  }

  public ToasterException(String msg) {
    errors = List.of(msg);
  }
}
