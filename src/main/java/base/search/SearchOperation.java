package base.search;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static java.util.Arrays.stream;


/**
 * Операции для поиска
 *
 * @author Ivan Zhendorenko
 */
@Getter
@RequiredArgsConstructor
public enum SearchOperation {
  EQUALITY(':'),
  NEGATION('!'),
  GREATER_THAN('>'),
  LESS_THAN('<'),
  LIKE('~');

  /**
   * Значение из query-строки запроса
   */
  public final char value;

  /**
   * Получить операцию по его строковому описанию
   *
   * @param input Строковое описание операции
   * @return Операция
   */
  public static SearchOperation from(final String input) {
    var ch = input.charAt(0);

    return stream(values())
        .filter(op -> op.getValue() == ch)
        .findFirst()
        .orElse(null);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
