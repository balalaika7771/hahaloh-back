package base.controller.crud.jpa;

import base.controller.abstractions.BaseController;
import base.service.jpa.CrudJpaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Collection;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * Контроллер с Delete-операциями
 *
 * @author Ivan Zhendorenko
 */
public interface DeleteController<D, E, I> extends BaseController<D, E> {

  /**
   * Сервис, используемый контроллером
   *
   * @return Сервис
   */
  @Override
  CrudJpaService<D, E, I> svc();

  @Operation(summary = "Удаление",
      description = "Удаление сущности по идентификатору",
      parameters = @Parameter(name = "id", description = "Идентификатор удаляемой сущности", required = true)
  )
  @PreAuthorize("hasPermission(#dummy, 'W') or hasPermission(#dummy, 'ADM')")
  @DeleteMapping("/delete-by-id/{id}")
  default void deleteById(@PathVariable I id, @Parameter(hidden = true) E dummy) {
    svc().deleteById(id);
  }

  @Operation(summary = "Удаление",
      description = "Удаление сущностей по идентификаторам",
      parameters = @Parameter(name = "ids", description = "Идентификаторы удаляемых сущностей в виде id1,id2,id3", required = true)
  )
  @PreAuthorize("hasPermission(#dummy, 'W') or hasPermission(#dummy, 'ADM')")
  @DeleteMapping("/delete-all-by-id/{ids}")
  default void deleteAllById(@PathVariable Collection<I> ids, @Parameter(hidden = true) E dummy) {
    svc().deleteAllById(ids);
  }
}
