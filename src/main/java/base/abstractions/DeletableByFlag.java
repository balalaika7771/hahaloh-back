package base.abstractions;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
 * Сущность, которая вместо удаления помечается как удалённая
 *
 * @author Ivan Zhendorenko
 */
@SuppressWarnings("unchecked")
@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
public abstract class DeletableByFlag<E> extends Identifiable<E> {

  public static final String IS_DELETED = "isDeleted";

  /**
   * Помечена ли сущность как удалённая
   */
  @Column(name = IS_DELETED, nullable = false)
  private Boolean isDeleted = false;

  public E setIsDeleted(Boolean deleted) {
    isDeleted = deleted;
    return (E) this;
  }
}
