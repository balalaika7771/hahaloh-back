package base.service.crudversioning;

import base.abstractions.VersionInfo;
import base.abstractions.Versionable;
import base.repository.VersioningRepository;
import base.service.abstractions.BaseVersioningService;
import base.service.jpa.UpdateJpaService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.function.Consumer;


/**
 * Сервис, способный изменять сущности
 *
 * <p>
 * {@inheritDoc}
 *
 * @author Ivan Zhendorenko
 */
@SuppressWarnings("unused")
public interface UpdateVersioningService<D, E extends Versionable<E>, I> extends BaseVersioningService<D, E, I>, UpdateJpaService<D, E, I> {

  EntityManager em();

  @Override
  VersioningRepository<E, I> repo();

  @Transactional
  @Override
  default E update(E entity, Consumer<E> mutator) {
    mutator.accept(prepareToUpdate(entity));

    return repo().save(entity);
  }

  @Transactional
  @Override
  default E updateAndFlush(E entity, Consumer<E> mutator) {
    mutator.accept(prepareToUpdate(entity));

    return repo().saveAndFlush(entity);
  }

  @Transactional
  @Override
  default List<E> updateAll(Iterable<E> entities, Consumer<E> mutator) {
    entities.forEach(this::prepareToUpdate);
    entities.forEach(mutator);

    return repo().saveAll(entities);
  }

  @Transactional
  @Override
  default List<E> updateAllAndFlush(Iterable<E> entities, Consumer<E> mutator) {
    entities.forEach(this::prepareToUpdate);
    entities.forEach(mutator);

    return repo().saveAllAndFlush(entities);
  }

  /**
   * Перестать отслеживать изменения сущности (для того, чтобы она сохранялась как новая), сбросить id для генерации новой записи
   *
   * @param entity Сущность
   */
  @SuppressWarnings("unchecked")
  default E prepareToUpdate(E entity) {
    var id = entity.getId();
    var current = repo().findVersionInfoById((I) id);
    var currentVersion = current
        .map(VersionInfo::getVersion)
        .orElse(-1);

    var branchId = current.map(VersionInfo::getBranchId)
        .orElse(0L);

    // Предыдущая версия помечается удалённой и скрывается от пользователя
    repo().deleteById((I) id);
    em().detach(entity);
    entity.setId(null)
        .setBranchId(branchId)
        .setPreviousId(id)
        .setVersion(currentVersion);

    return entity;
  }
}
