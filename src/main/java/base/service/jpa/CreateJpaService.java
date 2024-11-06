package base.service.jpa;

import base.service.abstractions.BaseJpaService;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;


/**
 * Сервис, способный создавать сущности
 *
 * @param <D> Тип дто
 * @param <E> Тип Сущности
 * @param <I> Тип идентификатора сущности
 * @author Ivan Zhendorenko
 */
public interface CreateJpaService<D, E, I> extends BaseJpaService<D, E, I> {

  // region entity

  @Transactional
  default E save(E entity) {
    return repo().save(entity);
  }

  @Transactional
  default E saveAndFlush(E entity) {
    return repo().saveAndFlush(entity);
  }

  @Transactional
  default List<E> saveAll(Iterable<E> entities) {
    return repo().saveAll(entities);
  }

  @Transactional
  default List<E> saveAllAndFlush(Iterable<E> entities) {
    return repo().saveAllAndFlush(entities);
  }

  // endregion

  // region dto

  @Transactional
  default D saveDto(E entity) {
    return enrich(t().entityToDto(save(entity)));
  }

  @Transactional
  default D saveAndFlushDto(E entity) {
    return enrich(t().entityToDto(saveAndFlush(entity)));
  }

  @Transactional
  default List<D> saveAllDto(Iterable<E> entities) {
    return t().entitiesToDtos(saveAll(entities)).stream().map(this::enrich).toList();
  }

  @Transactional
  default List<D> saveAllAndFlushDto(Iterable<E> entities) {
    return t().entitiesToDtos(saveAllAndFlush(entities)).stream().map(this::enrich).toList();
  }

  // endregion
}
