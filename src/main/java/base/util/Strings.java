package base.util;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;


/**
 * @author Ivan Zhendorenko
 */
@NoArgsConstructor(access = PRIVATE)
public class Strings {

  /**
   * Перевести строку из camelCase в snake_case
   *
   * @param str Входная строка
   * @return Выходная строка
   */
  public static String camelToSnake(String str) {
    var regex = "([a-z])([A-Z]+)";
    var replacement = "$1_$2";

    return str.replaceAll(regex, replacement)
        .toLowerCase();
  }
}
