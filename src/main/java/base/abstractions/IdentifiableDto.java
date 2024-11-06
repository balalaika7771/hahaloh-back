package base.abstractions;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.lang.NonNull;


/**
 * ДТО, имеющая идентификатор
 *
 * @param <T> Тип, который возвращает сеттер идентификатора
 * @author Ivan Zhendorenko
 */
@Getter
@Setter
@FieldNameConstants
public abstract class IdentifiableDto<T> {

  /**
   * Идентификатор
   */
  protected Long id;

  public T setId(@NonNull Long id) {
    this.id = id;
    return (T) this;
  }
}
