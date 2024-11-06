package base.controller.crud.jpa;

import base.controller.abstractions.BaseController;
import base.service.jpa.CrudJpaService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Collection;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * Контроллер с пакетной create-операцией
 *
 * @author Ivan Zhendorenko
 */
public interface MultiplyCreateController<D, E, I> extends BaseController<D, E> {

  /**
   * Сервис, используемый контроллером
   *
   * @return Сервис
   */
  @Override
  CrudJpaService<D, E, I> svc();

  @Operation(summary = "Сохранение коллекции",
      description = "Сохранение коллекции сущностей",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Коллекция с сохраняемыми сущностью",
          required = true
      )
  )
  @PostMapping("/save-all")
  default List<D> saveAll(@RequestBody Collection<D> dtos) {
    var entities = svc().t().dtosToEntities(dtos);
    return svc().saveAllDto(entities);
  }
}
