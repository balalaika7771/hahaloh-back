package base.abstractions;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
 * Сущность из сервиса, имеющая идентификатор оригинальной сущности из другого сервиса
 *
 * @author Ivan Zhendorenko
 */
@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
public abstract class OriginalIdIdentifiable<T> extends Identifiable<T> {

  /**
   * Идентификатор оригинальной сущности
   */
  @Column(name = "originalId", nullable = false)
  private Long originalId;

  @SuppressWarnings({"unchecked", "UnusedReturnValue"})
  public T setOriginalId(Long originalId) {
    this.originalId = originalId;
    return (T) this;
  }
}
