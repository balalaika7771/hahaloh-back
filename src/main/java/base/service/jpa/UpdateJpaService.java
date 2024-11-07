package base.service.jpa;

import base.service.abstractions.BaseJpaService;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.transaction.annotation.Transactional;


/**
 * Сервис, способный изменять сущности
 *
 * @param <D> Тип дто
 * @param <E> Тип Сущности
 * @param <I> Тип идентификатора сущности
 * @author Ivan Zhendorenko
 */
public interface UpdateJpaService<D, E, I> extends BaseJpaService<D, E, I> {

  //region entity

  @Transactional
  default E update(E entity, Consumer<E> mutator) {
    mutator.accept(entity);
    return repo().save(entity);
  }

  @Transactional
  default E updateAndFlush(E entity, Consumer<E> mutator) {
    mutator.accept(entity);
    return repo().saveAndFlush(entity);
  }

  @Transactional
  default List<E> updateAll(Iterable<E> entities, Consumer<E> mutator) {
    entities.forEach(mutator);
    return repo().saveAll(entities);
  }

  @Transactional
  default List<E> updateAllAndFlush(Iterable<E> entities, Consumer<E> mutator) {
    entities.forEach(mutator);
    return repo().saveAllAndFlush(entities);
  }

  //endregion

  //region dto

  @Transactional
  default D updateDto(E entity, Consumer<E> mutator) {
    return enrichDto(t().entityToDto(update(entity, mutator)));
  }

  @Transactional
  default D updateAndFlushDto(E entity, Consumer<E> mutator) {
    return enrichDto(t().entityToDto(updateAndFlush(entity, mutator)));
  }

  @Transactional
  default List<D> updateAllDto(Iterable<E> entities, Consumer<E> mutator) {
    return t().entitiesToDtos(updateAll(entities, mutator)).stream().map(this::enrichDto).toList();
  }

  @Transactional
  default List<D> updateAllAndFlushDto(Iterable<E> entities, Consumer<E> mutator) {
    return t().entitiesToDtos(updateAllAndFlush(entities, mutator)).stream().map(this::enrichDto).toList();
  }

  //endregion
}
