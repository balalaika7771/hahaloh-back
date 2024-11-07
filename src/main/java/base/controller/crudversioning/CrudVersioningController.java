package base.controller.crudversioning;

import base.abstractions.Versionable;
import base.service.crudversioning.CrudVersioningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Collection;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * Контроллер с CRUD-операциями над версионными сущностями
 *
 * @author Ivan Zhendorenko
 */
public interface CrudVersioningController<D extends Versionable<D>, E extends Versionable<E>, I> extends ReadVersioningController<D, E, I> {

  /**
   * Сервис, используемый контроллером
   *
   * @return Сервис
   */
  @Override
  CrudVersioningService<D, E, I> svc();

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

  @Operation(summary = "Сохранение коллекции",
      description = "Сохранение коллекции сущностей",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Коллекция с сохраняемыми сущностью",
          required = true
      )
  )
  @PreAuthorize("hasPermission(#dummy, 'W') or hasPermission(#dummy, 'ADM')")
  @PostMapping("/save-all")
  default List<D> saveAll(@RequestBody Collection<D> dtos, @Parameter(hidden = true) E dummy) {
    var entities = svc().t().dtosToEntities(dtos);
    return svc().saveAllDto(entities);
  }

  @Operation(summary = "Обновление",
      description = "Обновление сущности",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Обновляемая сущность",
          required = true
      )
  )
  @PreAuthorize("hasPermission(#dummy, 'W') or hasPermission(#dummy, 'ADM')")
  @PatchMapping("/update")
  default D update(@RequestBody D dto, @Parameter(hidden = true) E dummy) {
    var entity = svc().enrichEntity(svc().t().dtoToEntity(dto));
    return svc().updateDto(entity, e -> {
    });
  }

  @Operation(summary = "Удаление",
      description = "Удаление сущности по идентификатору",
      parameters = @Parameter(name = "id", description = "Идентификатор удаляемой сущности", required = true)
  )
  @PreAuthorize("hasPermission(#dummy, 'W') or hasPermission(#dummy, 'ADM')")
  @DeleteMapping("/delete-by-id/{id}")
  default D deleteById(@PathVariable I id, @Parameter(hidden = true) E dummy) {
    return svc().deleteByIdDto(id);
  }

  @Operation(summary = "Удаление",
      description = "Удаление сущностей по идентификаторам",
      parameters = @Parameter(name = "ids", description = "Идентификаторы удаляемых сущностей в виде id1,id2,id3", required = true)
  )
  @PreAuthorize("hasPermission(#dummy, 'W') or hasPermission(#dummy, 'ADM')")
  @DeleteMapping("/delete-all-by-id/{ids}")
  default List<D> deleteAllById(@PathVariable Collection<I> ids, @Parameter(hidden = true) E dummy) {
    return svc().deleteAllByIdDto(ids);
  }
}
