package base.search;

import base.exception.ToasterException;
import base.extension.ArrayExtensions;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.ExtensionMethod;
import org.springframework.data.jpa.domain.Specification;

import static base.search.SearchCriteria.PATH_SEPARATOR;


/**
 * Спецификация для работы с операциями сравнения
 *
 * @param <E> Тип сущности
 * @author Ivan Zhendorenko
 */
@AllArgsConstructor
@ExtensionMethod({ArrayExtensions.class})
public class CustomSpecification<E> implements Specification<E> {

  private static final String LONG_NAME = "java.lang.Long";

  private static final String INT_NAME = "java.lang.Integer";

  private static final String STR_NAME = "java.lang.String";

  private static final String BOOL_NAME = "java.lang.Boolean";

  private transient SearchCriteria c;

  @Override
  public Predicate toPredicate(@NonNull Root<E> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
    query.distinct(c.isDistinct());

    var propPath = c.getKey();
    From<?, ?> body = root;

    // Сложное поле, надо джойниться для фильтра
    if (c.isComplex()) {
      var elements = List.of(propPath.split(PATH_SEPARATOR));

      propPath = elements.get(elements.size() - 1);  // Последний элемент как propPath

      for (int i = 0; i < elements.size() - 1; i++) {
        var e = elements.get(i);
        body = body.join(e);  // Джойнимся к каждой части пути, кроме последней
      }
    }

    return finalFilter(cb, body, propPath);
  }


  private Predicate finalFilter(CriteriaBuilder b, From<?, ?> r, String prop) {
    var actualType = r.get(prop).getJavaType();
    var rawValue = c.getValue();
    var castedValue = tryCast(actualType, rawValue);

    var op = checkAndGetOpOrThrow(castedValue, actualType.getName());

    return switch (op) {
      case EQUALITY -> castedValue == null
          ? b.isNull(r.get(prop))
          : b.equal(r.get(prop), castedValue);

      case NEGATION -> b.notEqual(r.get(prop), castedValue);

      case GREATER_THAN -> b.greaterThan(r.get(prop), String.valueOf(castedValue));

      case LESS_THAN -> b.lessThan(r.get(prop), String.valueOf(castedValue));

      case LIKE -> b.like(b.lower(r.get(prop)
          .as(String.class)), "%" + String.valueOf(castedValue).toLowerCase() + "%");
    };
  }

  private <T> Object tryCast(Class<T> actualType, Object val) {
    if (val == null) return null;

    if (actualType.isEnum()) return val;

    return switch (actualType.getName()) {
      case STR_NAME -> val.toString();

      case LONG_NAME -> Long.valueOf(val.toString());

      case BOOL_NAME -> Boolean.valueOf(val.toString());

      case INT_NAME -> Integer.valueOf(val.toString());

      default -> actualType.cast(val);
    };
  }

  private SearchOperation checkAndGetOpOrThrow(Object castedValue, String actualTypeName) {
    var op = c.getOperation();

    if (castedValue == null) switch (op) {
      case EQUALITY, NEGATION -> {
      }
      case LIKE, GREATER_THAN, LESS_THAN -> {
        var msg = "Поле %s имеет тип %s, использованная операция %s, значение null. Данный предикат не валиден".formatted(c.getKey(), actualTypeName, op);
        throw new ToasterException(msg);
      }
    }

    return op;
  }
}