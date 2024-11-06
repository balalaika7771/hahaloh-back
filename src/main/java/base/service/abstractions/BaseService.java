package base.service.abstractions;

import base.transformer.Transformer;


/**
 * Базовая абстракция для любого сервиса
 *
 * @param <D> Тип дто
 * @param <E> Тип Сущности
 * @author Ivan Zhendorenko
 */
public interface BaseService<D, E> {


  /**
   * Трансформер дто <-> бд-сущность
   *
   * @return Трансформер
   */
  Transformer<D, E> t();

  /**
   * Обогатить dto какими-либо данными
   *
   * @param dto dto
   * @return Обогащённая dto
   */
  default D enrich(D dto) {
    return dto;
  }
}
