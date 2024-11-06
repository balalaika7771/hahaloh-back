package base.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static base.search.SearchCriteria.PATH_SEPARATOR;
import static org.springframework.data.jpa.domain.Specification.where;


/**
 * Билдер для построения спецификации для поиска
 *
 * @param <E> Тип сущности
 * @author Ivan Zhendorenko
 */
@RequiredArgsConstructor
public class SpecificationBuilder<E> {

  private final List<SearchCriteria> params = new ArrayList<>();

  private final Map<String, String> pathReplacers;

  /**
   * Заполнитель описания спецификации для фильтрации
   *
   * @param key       Название поля
   * @param operation Операция
   * @param rawValue  Значение поля
   * @return Билдер с заполненной спецификацией
   */
  public SpecificationBuilder<E> with(String key, String operation, Object rawValue, boolean orPredicate) {
    return with(key, operation, rawValue, orPredicate, false);
  }

  /**
   * @see #with(String, String, Object, boolean)
   */
  public SpecificationBuilder<E> with(String key, SearchOperation operation, Object rawValue, boolean orPredicate) {
    return with(key, operation.toString(), rawValue, orPredicate, false);
  }

  /**
   * Заполнитель описания спецификации для фильтрации c возможностью вывода уникальных значений
   *
   * @param path       Название поля
   * @param operation  Операция
   * @param rawValue   Значение поля
   * @param isDistinct Вывод только уникальных значений
   * @return Билдер с заполненной спецификацией
   */
  public SpecificationBuilder<E> with(String path, String operation, Object rawValue, boolean orPredicate, boolean isDistinct) {
    var op = SearchOperation.from(operation);
    // Придумать, как реализовать связку OR в DSL (и нужно ли)

    Object finalValue;
    if (rawValue instanceof String s) {
      if ("true".equals(s)) {
        finalValue = true;
      } else if ("false".equals(s)) {
        finalValue = false;
      } else if ("null".equals(s)) {
        finalValue = null;
      } else {
        finalValue = rawValue;
      }
    } else {
      finalValue = rawValue;
    }


    params.add(new SearchCriteria(tryMutatePath(path), op, finalValue, orPredicate, isDistinct));

    return this;
  }

  /**
   * Попытка мутировать путь
   * <p>
   * Сначала идет попытка полной замены пути, затем попытка частичной замены пути
   *
   * @param path Оригинальный путь с фронта по dto
   * @return Мутированный путь, либо оригинал пути
   */
  private String tryMutatePath(String path) {
    var result = path;

    if (!path.contains(PATH_SEPARATOR)) return result;

    var isPresent = pathReplacers.containsKey(path);
    if (isPresent) result = pathReplacers.get(path);

    if (!isPresent) {
      var pathWithReplacedPart = pathReplacers.entrySet().stream()
          .filter(e -> path.contains(e.getKey()))
          .findFirst()
          .map(p -> path.replace(p.getKey(), p.getValue()));

      if (pathWithReplacedPart.isPresent()) result = pathWithReplacedPart.get();
    }

    return result;
  }

  /**
   * Собрать спецификацию по сформированным правилам
   *
   * @return Спецификация для поиска
   */
  public Specification<E> build() {
    if (params.isEmpty()) return null;

    var specs = params.stream()
        .map(CustomSpecification<E>::new)
        .toList();

    Specification<E> result = specs.get(0);
    for (int i = 1; i < params.size(); i++)
      result = params.get(i).isOrPredicate()
          ? where(result).or(specs.get(i))
          : where(result).and(specs.get(i));

    return result;
  }
}
