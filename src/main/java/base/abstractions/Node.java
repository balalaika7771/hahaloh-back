package base.abstractions;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Reference;


/**
 * Сущность, имеющая древовидную структуру
 *
 * @author Ivan Zhendorenko
 */
@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
public abstract class Node<E> extends Identifiable<E> {

  /**
   * Идентификатор родительского элемента
   */
  @Column(name = "parentId")
  private Long parentId;

  /**
   * Родительский элемент
   */
  @Reference
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parentId", insertable = false, updatable = false)
  private E parent;

  /**
   * Дочерние элементы
   */
  @Reference
  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
  @Column(insertable = false, updatable = false)
  private List<E> children;
}
