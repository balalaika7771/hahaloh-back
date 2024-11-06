package base.controller.abstractions;

import base.service.abstractions.BaseService;


/**
 * Базовая абстракция для любого контроллера
 *
 * @param <D> Тип дто
 * @param <E> Тип Сущности
 * @author Ivan Zhendorenko
 */
public interface BaseController<D, E> {

  /**
   * Сервис, используемый контроллером
   *
   * @return Сервис
   */
  BaseService<D, E> svc();
}
