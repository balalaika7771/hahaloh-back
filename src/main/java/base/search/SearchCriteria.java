package base.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;


/**
 * Элемента критерия для поиска по {@link Specification}, промежуточная обёртка используемая в {@link SpecificationBuilder}
 *
 * @author Ivan Zhendorenko
 */
@Data
@AllArgsConstructor
public class SearchCriteria {

  public static final String PATH_SEPARATOR = ".";

  /**
   * Название поля
   */
  private String key;

  /**
   * Операция фильтрации
   */
  private SearchOperation operation;

  /**
   * Значение для операции фильтрации
   */
  private Object value;

  /**
   * Связан ли критерий связкой or
   */
  private boolean orPredicate;

  /**
   * Требуется ли убрать дубли из вывода
   */
  private boolean isDistinct;

  public boolean isComplex() {
    return key.contains(PATH_SEPARATOR);
  }
}
