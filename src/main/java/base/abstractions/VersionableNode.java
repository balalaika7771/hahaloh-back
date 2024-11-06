package base.abstractions;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
 * Сущность, имеющая древовидную структуру
 *
 * @param <E> Тип самой сущности
 * @author Ivan Zhendorenko
 */
@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
public abstract class VersionableNode<E> extends Versionable<E> {

  /**
   * Идентификатор родительского элемента
   */
  @Column(name = "parentId")
  private Long parentId;

  /**
   * Родительский элемент
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parentId", updatable = false, insertable = false)
  private E parent;

  /**
   * Дочерние элементы
   */
  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
  @Column(insertable = false, updatable = false)
  private List<E> children;

  public E setParentId(Long parentId) {
    this.parentId = parentId;
    return (E) this;
  }

  public E setParent(E parent) {
    this.parent = parent;
    return (E) this;
  }

  public E setChildren(List<E> children) {
    this.children = children;
    return (E) this;
  }

  @PostLoad
  protected void onPostLoad() {
    //noinspection unchecked
    children = children.stream()
        .filter(c -> !((DeletableByFlag<E>) c).getIsDeleted())
        .toList();
  }
}
