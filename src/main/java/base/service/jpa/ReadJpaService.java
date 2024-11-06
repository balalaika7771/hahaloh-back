package base.service.jpa;

import base.service.abstractions.BaseJpaService;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;


/**
 * Сервис, способный отдавать сущности
 *
 * @param <D> Тип дто
 * @param <E> Тип Сущности
 * @param <I> Тип идентификатора сущности
 * @author Ivan Zhendorenko
 */
public interface ReadJpaService<D, E, I> extends BaseJpaService<D, E, I> {


  // region entity

  default Optional<E> findById(I id) {
    return repo().findById(id);
  }

  default List<E> findAllById(Iterable<I> ids) {
    return repo().findAllById(ids);
  }

  default List<E> findAll(Sort sort) {
    return repo().findAll(sort);
  }

  default Page<E> findAll(Pageable pageable) {
    return repo().findAll(pageable);
  }

  default <S extends E> Optional<S> findOne(Example<S> example) {
    return repo().findOne(example);
  }

  default <S extends E> Iterable<S> findAll(Example<S> example) {
    return repo().findAll(example);
  }

  default <S extends E> Iterable<S> findAll(Example<S> example, Sort sort) {
    return repo().findAll(example, sort);
  }

  default <S extends E> Page<S> findAll(Example<S> example, Pageable pageable) {
    return repo().findAll(example, pageable);
  }

  default <S extends E> long count(Example<S> example) {
    return repo().count();
  }

  default <S extends E> boolean exists(Example<S> example) {
    return repo().exists(example);
  }

  default <S extends E, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
    return repo().findBy(example, queryFunction);
  }

  // endregion

  // region dto

  default Optional<D> findByIdDto(I id) {
    return t().entityToDto(findById(id)).map(this::enrich);
  }

  default List<D> findAllByIdDto(Iterable<I> ids) {
    return t().entitiesToDtos(findAllById(ids)).stream().map(this::enrich).toList();
  }

  default List<D> findAllDto(Sort sort) {
    return t().entitiesToDtos(findAll(sort)).stream().map(this::enrich).toList();
  }

  default Page<D> findAllDto(Pageable pageable) {
    return t().entityToDtoPage(findAll(pageable)).map(this::enrich);
  }

  // endregion
}
