package base.service.crudversioning;

import base.abstractions.Versionable;
import base.repository.VersioningRepository;
import base.search.SpecificationBuilder;
import base.service.abstractions.BaseVersioningService;
import base.service.jpa.ReadJpaService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import static base.abstractions.DeletableByFlag.IS_DELETED;
import static base.abstractions.Versionable.BRANCH_ID;
import static base.search.SearchOperation.EQUALITY;


/**
 * Сервис, способный отдавать сущности, практически всё апи переопределено так, чтобы работать с сущностями с isDeleted == false
 *
 * <p>
 * {@inheritDoc}
 *
 * @author Ivan Zhendorenko
 */
@SuppressWarnings("unused")
public interface ReadVersioningService<D, E extends Versionable<E>, I> extends BaseVersioningService<D, E, I>, ReadJpaService<D, E, I> {

  @Override
  VersioningRepository<E, I> repo();

  // region entity

  @Override
  default Optional<E> findById(I id) {
    var spec = new SpecificationBuilder<E>(searchPathReplacer())
        .with("id", EQUALITY.toString(), id, false)
        .with(IS_DELETED, EQUALITY.toString(), false, false)
        .build();

    return repo().findOne(spec);
  }

  @Override
  default List<E> findAllById(Iterable<I> ids) {
    return repo().findAllById(ids);
  }

  @Override
  default List<E> findAll(Sort sort) {
    var spec = new SpecificationBuilder<E>(searchPathReplacer())
        .with(IS_DELETED, EQUALITY.toString(), false, false)
        .build();

    return repo().findAll(spec, sort);
  }

  @Override
  default Page<E> findAll(Pageable pageable) {
    var spec = new SpecificationBuilder<E>(searchPathReplacer())
        .with(IS_DELETED, EQUALITY.toString(), false, false)
        .build();

    return repo().findAll(spec, pageable);
  }

  @Override
  default List<E> findAll(Specification<E> spec) {
    var deletedSpec = new SpecificationBuilder<E>(searchPathReplacer())
        .with(IS_DELETED, EQUALITY.toString(), false, false)
        .build();

    return ReadJpaService.super.findAll(spec.and(deletedSpec));
  }

  @Override
  default Page<E> findAll(Specification<E> spec, Pageable pageable) {
    var deletedSpec = new SpecificationBuilder<E>(searchPathReplacer())
        .with(IS_DELETED, EQUALITY.toString(), false, false)
        .build();

    return ReadJpaService.super.findAll(spec.and(deletedSpec), pageable);
  }

  @Override
  default <S extends E> Optional<S> findOne(Example<S> example) {
    return repo().findBy(example, q -> q.stream()
        .filter(e -> !e.getIsDeleted())
        .findFirst()
    );
  }

  @Override
  default Optional<E> findOne(Specification<E> spec) {
    var deletedSpec = new SpecificationBuilder<E>(searchPathReplacer())
        .with(IS_DELETED, EQUALITY.toString(), false, false)
        .build();

    return repo().findOne(spec.and(deletedSpec));
  }

  @Override
  default <S extends E> Iterable<S> findAll(Example<S> example) {
    return repo().findBy(example, q -> q.stream()
        .filter(e -> !e.getIsDeleted())
        .toList()
    );
  }

  @Override
  default <S extends E> Iterable<S> findAll(Example<S> example, Sort sort) {
    return repo().findBy(example, q -> q.sortBy(sort)
        .stream()
        .filter(e -> !e.getIsDeleted())
        .toList()
    );
  }

  /**
   * Поиск актуальной версии по идентификатору ветки версий
   *
   * @param branchId Идентификатор ветки версий
   * @return Актуальная версия
   */
  default Optional<E> findActualByBranchId(@NonNull Long branchId) {
    var spec = new SpecificationBuilder<E>(searchPathReplacer())
        .with(BRANCH_ID, EQUALITY, branchId, false)
        .with(IS_DELETED, EQUALITY, false, false)
        .build();

    return repo().findOne(spec);
  }

  // endregion

  // region dto

  @Override
  default Optional<D> findByIdDto(I id) {
    return t().entityToDto(findById(id)).map(this::enrich);
  }

  @Override
  default List<D> findAllByIdDto(Iterable<I> ids) {
    return t().entitiesToDtos(findAllById(ids)).stream().map(this::enrich).toList();
  }

  @Override
  default List<D> findAllDto(Sort sort) {
    return t().entitiesToDtos(findAll(sort)).stream().map(this::enrich).toList();
  }

  @Override
  default Page<D> findAllDto(Pageable pageable) {
    return t().entityToDtoPage(findAll(pageable)).map(this::enrich);
  }

  @Override
  default List<D> findAllDto(Specification<E> spec) {
    return t().entitiesToDtos(findAll(spec)).stream().map(this::enrich).toList();
  }

  @Override
  default Page<D> findAllDto(Specification<E> spec, Pageable pageable) {
    return t().entityToDtoPage(findAll(spec, pageable)).map(this::enrich);
  }

  @Override
  default Optional<D> findOneDto(Specification<E> spec) {
    return t().entityToDto(findOne(spec)).map(this::enrich);
  }

  /**
   * Поиск актуальной версии по идентификатору ветки версий
   *
   * @param branchId Идентификатор ветки версий
   * @return Актуальная версия
   */
  default Optional<D> findActualByBranchIdDto(@lombok.NonNull Long branchId) {
    return t().entityToDto(findActualByBranchId(branchId)).map(this::enrich);
  }

  // endregion
}