package base.controller.crud.jpa;

import base.controller.abstractions.BaseController;
import base.service.jpa.CrudJpaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * Контроллер с Update-операциями
 *
 * @author Ivan Zhendorenko
 */
public interface UpdateController<D, E, I> extends BaseController<D, E> {


  /**
   * Сервис, используемый контроллером
   *
   * @return Сервис
   */
  @Override
  CrudJpaService<D, E, I> svc();

  @Operation(summary = "Обновление",
      description = "Обновление сущности",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Обновляемая сущность",
          required = true
      )
  )
  @PatchMapping("/update")
  default D update(@RequestBody D dto) {
    var entity = svc().t().dtoToEntity(dto);
    return svc().updateDto(entity, e -> {
    });
  }
}
