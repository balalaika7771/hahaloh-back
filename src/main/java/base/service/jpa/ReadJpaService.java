package base.service.jpa;

import base.repository.JpaSpecificationExecutorRepository;
import base.service.abstractions.BaseJpaService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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


  /**
   * Репозиторий
   *
   * @return Репозиторий
   */
  @Override
  JpaSpecificationExecutorRepository<E, I> repo();

  /**
   * Мапа для замены путей в сёрче для ситуаций, когда путь внутри дто отличается от пути внутри сущности
   * <p>
   *
   * @return Полный путь или его часть для поиска в жсоне - путь для поиска в сущности (не должно быть transient-полей)
   */
  default Map<String, String> searchPathReplacer() {
    return Map.of();
  }

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

  default List<E> findAll(Specification<E> spec) {
    return repo().findAll(spec);
  }

  default Page<E> findAll(Specification<E> spec, Pageable pageable) {
    return repo().findAll(spec, pageable);
  }

  default <S extends E> Optional<S> findOne(Example<S> example) {
    return repo().findOne(example);
  }

  default Optional<E> findOne(Specification<E> spec) {
    return repo().findOne(spec);
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

  default List<D> findAllDto(Specification<E> spec) {
    return t().entitiesToDtos(findAll(spec)).stream().map(this::enrich).toList();
  }

  default Page<D> findAllDto(Specification<E> spec, Pageable pageable) {
    return t().entityToDtoPage(findAll(spec, pageable)).map(this::enrich);
  }

  default Optional<D> findOneDto(Specification<E> spec) {
    return t().entityToDto(findOne(spec)).map(this::enrich);
  }

  default Optional<D> findOneDto(Example<E> example) {
    return t().entityToDto(findOne(example)).map(this::enrich);
  }

  default Iterable<D> findAllDto(Example<E> example) {
    return t().entitiesToDtos(IterableUtils.toList(findAll(example))).stream().map(this::enrich).toList();
  }

  default Iterable<D> findAllDto(Example<E> example, Sort sort) {
    return t().entitiesToDtos(IterableUtils.toList(findAll(example, sort))).stream().map(this::enrich).toList();
  }

  default Page<D> findAllDto(Example<E> example, Pageable pageable) {
    return t().entityToDtoPage(findAll(example, pageable)).map(this::enrich);
  }

  // endregion
}
