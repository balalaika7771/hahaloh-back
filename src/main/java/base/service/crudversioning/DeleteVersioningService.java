package base.service.crudversioning;

import base.abstractions.Identifiable;
import base.abstractions.Versionable;
import base.repository.VersioningRepository;
import base.service.abstractions.BaseVersioningService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;


/**
 * Сервис, способный удалять сущности
 *
 * <p>
 * {@inheritDoc}
 *
 * @author Ivan Zhendorenko
 */
@SuppressWarnings("unused")
public interface DeleteVersioningService<D, E extends Versionable<E>, I> extends BaseVersioningService<D, E, I> {

  VersioningRepository<E, I> repo();

  // region entity

  default E deleteById(I id) {
    repo().deleteById(id);
    return repo().findById(id).orElse(null);
  }

  default E delete(E entity) {
    repo().delete(entity);
    return repo().findById((I) entity.getId()).orElse(null);
  }

  default List<E> deleteAllById(Iterable<I> ids) {
    return repo().defaultFindAllById(ids);
  }

  default List<E> deleteAll(Iterable<E> entities) {
    var ids = (ArrayList<I>) StreamSupport.stream(entities.spliterator(), false).map(Identifiable::getId).toList();
    deleteAllById(ids);
    return repo().defaultFindAllById(ids);
  }

  default List<E> deleteAll() {
    repo().deleteAll();
    return repo().findAll();
  }

  // endregion

  // region dto

  default D deleteByIdDto(I id) {
    return enrichDto(t().entityToDto(deleteById(id)));
  }

  default D deleteDto(E entity) {
    return enrichDto(t().entityToDto(delete(entity)));
  }

  default List<D> deleteAllByIdDto(Iterable<I> ids) {
    return t().entitiesToDtos(deleteAllById(ids)).stream().map(this::enrichDto).toList();
  }

  default List<D> deleteAllDto(Iterable<E> entities) {
    return t().entitiesToDtos(deleteAll(entities)).stream().map(this::enrichDto).toList();
  }

  default List<D> deleteAllDto() {
    return t().entitiesToDtos(deleteAll()).stream().map(this::enrichDto).toList();
  }

  // endregion
}
