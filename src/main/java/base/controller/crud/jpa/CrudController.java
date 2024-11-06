package base.controller.crud.jpa;


import base.service.jpa.CrudJpaService;


/**
 * Контроллер с CRUD-операциями
 *
 * @author Ivan Zhendorenko
 */
public interface CrudController<D, E, I> extends CudController<D, E, I>, ReadController<D, E, I> {

  /**
   * Сервис, используемый контроллером
   *
   * @return Сервис
   */
  @Override
  CrudJpaService<D, E, I> svc();
}
