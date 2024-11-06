package base.transformer;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;


/**
 * Трансформер дто <-> бд-сущность
 *
 * @param <D> Тип дто
 * @param <E> Тип сущности
 * @author Ivan Zhendorenko
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public interface Transformer<D, E> {

  /**
   * Сущность -> дто
   *
   * @param entity Сущность
   * @return Дто
   */
  D entityToDto(E entity);

  /**
   * Дто -> сущность
   *
   * @param dto Дто
   * @return Сущность
   */
  E dtoToEntity(D dto);

  /**
   * Сущность -> дто
   *
   * @param entityOpt Сущность
   * @return Дто
   */
  default Optional<D> entityToDto(Optional<E> entityOpt) {
    return entityOpt.map(this::entityToDto);
  }

  /**
   * Дто -> сущность
   *
   * @param dtoOpt Дто
   * @return Сущность
   */
  default Optional<E> dtoToEntity(Optional<D> dtoOpt) {
    return dtoOpt.map(this::dtoToEntity);
  }

  /**
   * Страница с сущностями -> страницу с дто
   *
   * @param entityPage Страница с сущностями
   * @return Страница с дто
   */
  default Page<D> entityToDtoPage(Page<E> entityPage) {
    return new PageImpl<>(
        entitiesToDtos(entityPage.getContent()),
        entityPage.getPageable(),
        entityPage.getTotalElements()
    );
  }

  /**
   * Коллекция сущностей -> коллекция дто
   *
   * @param entities Коллекция сущностей
   * @return Коллекция дто
   */
  default List<D> entitiesToDtos(Collection<E> entities) {
    return Objects.isNull(entities) ? null
        : entities.stream()
                  .map(this::entityToDto)
                  .toList();
  }

  /**
   * Коллекция дто -> коллекция сущностей
   *
   * @param dtos Коллекция дто
   * @return Коллекция сущностей
   */
  default List<E> dtosToEntities(Collection<D> dtos) {
    return Objects.isNull(dtos) ? null
        : dtos.stream()
              .map(this::dtoToEntity)
              .toList();
  }
}