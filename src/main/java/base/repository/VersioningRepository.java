package base.repository;

import base.abstractions.VersionInfo;
import base.abstractions.Versionable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;


/**
 * Абстракция для работы с версионируемыми сущностями
 * <p>
 * {@inheritDoc}
 *
 * @author Ivan Zhendorenko
 */
@NoRepositoryBean
public interface VersioningRepository<E extends Versionable<E>, I> extends JpaSpecificationExecutorRepository<E, I> {

  /**
   * {@inheritDoc}
   *
   * @param ids must not be {@literal null} nor contain any {@literal null} values.
   * @return
   */
  @Query("from #{#entityName} e where e.id in :ids and e.isDeleted = false")
  @Override
  List<E> findAllById(@Param("ids") @NonNull Iterable<I> ids);

  /**
   * Стандартный поиск по всем элементам, игнорирующий логику версионных сущностей
   *
   * @param ids Идентификаторы
   * @return Элементы
   */
  @Query("from #{#entityName} e where e.id in :ids")
  List<E> defaultFindAllById(@Param("ids") @NonNull Iterable<I> ids);

  /**
   * {@inheritDoc}
   *
   * @param id must not be {@literal null}.
   */
  @Modifying
  @Query("update #{#entityName} e set e.isDeleted = true where e.id = :id")
  void deleteById(@Param("id") @NonNull I id);

  /**
   * {@inheritDoc}
   *
   * @param entity must not be {@literal null}.
   */
  @Modifying
  @Query("update #{#entityName} e set e.isDeleted = true where e.id = :#{#entity.id}")
  void delete(@NonNull E entity);

  /**
   * {@inheritDoc}
   *
   * @param ids must not be {@literal null}. Must not contain {@literal null} elements.
   */
  @Modifying
  @Query("update #{#entityName} e set e.isDeleted = true where e.id in :ids")
  void deleteAllById(@Param("ids") @NonNull Iterable<? extends I> ids);

  /**
   * Ничего не делает, пока не придумал, как реализовать
   */
  @Override
  default void deleteAll(@NonNull Iterable<? extends E> entities) {
  }

  /**
   * {@inheritDoc}
   */
  @Modifying
  @Query("update #{#entityName} e set e.isDeleted = true")
  void deleteAll();

  /**
   * Найти проекцию с версионными полями
   *
   * @param id Идентификатор сущности
   * @return Проекция с версионными полями
   */
  Optional<VersionInfo> findVersionInfoById(I id);
}
