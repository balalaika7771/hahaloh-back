package base.util;

import base.search.SpecificationBuilder;
import java.util.Map;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;

import static base.constants.Data.SEARCH_PATTERN;
import static java.util.regex.Pattern.compile;
import static org.springframework.data.domain.ExampleMatcher.matching;


/**
 * @author Ivan Zhendorenko
 */
@UtilityClass
public class Data {

  /**
   * @see ExampleMatcher#withIgnoreNullValues()
   */
  public static ExampleMatcher ignoreNullMatcher() {
    return matching().withIgnoreNullValues();
  }

  /**
   * Пример для поиска, игнорирующий пустые поля
   * <p>
   * Аналогично:
   * <pre>
   * {@code Example.of(entity, ExampleMatcher.withIgnoreNullValues())}
   * </pre>
   */
  public static <E> Example<E> ignoreNullExample(E entity) {
    return Example.of(entity, ignoreNullMatcher());
  }

  /**
   * Спарсить строку поиска в спецификацию для селекта
   *
   * @param search       Строка поиска
   * @param pathReplacer Мапа для замены путей в сёрче для ситуаций, когда путь внутри дто отличается от пути внутри сущности
   * @param isDistinct   Вывод только уникальных значений
   * @param <E>          Тип сущности
   * @return Спецификация
   */
  @SuppressWarnings("rawtypes")
  public static Specification searchToSpec(@NonNull String search, @NonNull Map<String, String> pathReplacer, boolean isDistinct) {
    var pattern = compile(SEARCH_PATTERN);
    var matcher = pattern.matcher(search + ",");

    var builder = new SpecificationBuilder<>(pathReplacer);
    while (matcher.find())
      builder = builder.with(matcher.group(1), matcher.group(2), matcher.group(3), false, isDistinct);

    return builder.build();
  }

  /**
   * @see Data#searchToSpec(String, Map, boolean)
   */
  @SuppressWarnings("rawtypes")
  public static Specification searchToSpec(@NonNull String search, boolean isDistinct) {
    return searchToSpec(search, Map.of(), isDistinct);
  }
}
