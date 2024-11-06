package base.service.jpa;


import base.service.abstractions.BaseJpaService;


/**
 * Сервис, способный удалять сущности
 *
 * @param <D> Тип дто
 * @param <E> Тип Сущности
 * @param <I> Тип идентификатора сущности
 * @author Ivan Zhendorenko
 */
public interface DeleteJpaService<D, E, I> extends BaseJpaService<D, E, I> {

  default void deleteById(I id) {
    repo().deleteById(id);
  }

  default void delete(E entity) {
    repo().delete(entity);
  }

  default void deleteAllById(Iterable<I> ids) {
    repo().deleteAllById(ids);
  }

  default void deleteAll(Iterable<E> entities) {
    repo().deleteAll(entities);
  }

  default void deleteAll() {
    repo().deleteAll();
  }
}
