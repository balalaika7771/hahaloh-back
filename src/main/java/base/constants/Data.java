package base.constants;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;


@NoArgsConstructor(access = PRIVATE)
public class Data {

  /**
   * Регулярное выражение для парсинга фильтров
   */

  public static final String SEARCH_PATTERN = "(\\S+?)([:<>~!])(([\\w\\s\\d]|[а-яёА-ЯЁ]|[\\\\/'№\\-@\"])+?),";
}
