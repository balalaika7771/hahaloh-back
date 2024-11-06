package base.abstractions;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Reference;

import static jakarta.persistence.FetchType.LAZY;


/**
 * Версионируемая сущность
 *
 * @author Ivan Zhendorenko
 */
@SuppressWarnings("unchecked")
@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
public abstract class Versionable<E> extends DeletableByFlag<E> {

  public static final String BRANCH_ID = "branchId";

  public static final String PREVIOUS_ID = "previousId";

  /**
   * Версия сущности
   */
  @NotNull
  @Column(name = "version", nullable = false)
  private Integer version = -1;

  /**
   * Идентификатор предыдущей версии
   */
  @Column(name = PREVIOUS_ID)
  private Long previousId;

  /**
   * Предыдущая версия
   */
  @Reference
  @OneToOne(fetch = LAZY)
  @JoinColumn(name = PREVIOUS_ID, updatable = false, insertable = false)
  private E previous;

  /**
   * Идентификатор ветки версий сущности {@link E}
   */
  @Column(name = BRANCH_ID)
  private Long branchId;

  public E setBranchId(Long branchId) {
    this.branchId = branchId;
    return (E) this;
  }

  public E setVersion(Integer version) {
    this.version = version;
    return (E) this;
  }

  public E setPreviousId(Long previousId) {
    this.previousId = previousId;
    return (E) this;
  }

  public E setPrevious(E previous) {
    this.previous = previous;
    return (E) this;
  }

  /**
   * Получить историю со всеми предыдущими версиями
   *
   * @return История со всеми предыдущими версиями
   */
  public List<E> getHistory() {
    var history = new ArrayList<E>();

    if (this != previous && previous != null) {
      history.add(previous);
      var casted = (Versionable<E>) previous;
      history.addAll(casted.getHistory());
    }

    return history;
  }

  @PrePersist
  protected void prePersist() {
    customPrePersist();
    version++;
  }

  protected void customPrePersist() {
  }
}
