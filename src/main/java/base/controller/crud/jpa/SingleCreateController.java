package base.controller.crud.jpa;

import base.controller.abstractions.BaseController;
import base.service.jpa.CrudJpaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * Контроллер с одиночной create-операцией
 *
 * @author Ivan Zhendorenko
 */
public interface SingleCreateController<D, E, I> extends BaseController<D, E> {

  /**
   * Сервис, используемый контроллером
   *
   * @return Сервис
   */
  @Override
  CrudJpaService<D, E, I> svc();

  @Operation(summary = "Сохранение",
      description = "Сохранение сущности",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Сохраняемая сущность",
          required = true
      )
  )
  @PreAuthorize("hasPermission(#dummy, 'W') or hasPermission(#dummy, 'ADM')")
  @PostMapping("/save")
  default D save(@RequestBody D dto, @Parameter(hidden = true) E dummy) {
    var entity = svc().enrichEntity(svc().t().dtoToEntity(dto));
    return svc().saveDto(entity);
  }
}
