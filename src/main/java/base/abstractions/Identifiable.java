package base.abstractions;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
 * Сущность, имеющая идентификатор
 *
 * @author Ivan Zhendorenko
 */
@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
public abstract class Identifiable<T> implements Serializable {

  /**
   * Идентификатор
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @SuppressWarnings({"unchecked", "UnusedReturnValue"})
  public T setId(Long id) {
    this.id = id;
    return (T) this;
  }
}
