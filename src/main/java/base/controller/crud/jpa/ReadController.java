package base.controller.crud.jpa;

import base.controller.abstractions.BaseController;
import base.service.jpa.ReadJpaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


public interface ReadController<D, E, I> extends BaseController<D, E>, SearchController<D, E, I> {

  /**
   * Сервис, используемый контроллером
   *
   * @return Сервис
   */
  @Override
  ReadJpaService<D, E, I> svc();

  @Operation(summary = "Поиск",
      description = "Поиск сущности по идентификатору",
      parameters = @Parameter(name = "id", description = "Идентификатор сущности", required = true)
  )
  @PreAuthorize("hasPermission(#dummy, 'R') or hasPermission(#dummy, 'ADM')")
  @GetMapping("/find-by-id/{id}")
  default Optional<D> findById(@PathVariable I id, @Parameter(hidden = true) E dummy) {
    return svc().findByIdDto(id);
  }

  @Operation(summary = "Поиск",
      description = "Поиск сущностей по идентификаторам",
      parameters = @Parameter(name = "ids", description = "Идентификаторы искомых сущностей в виде id1,id2,id3", required = true)
  )
  @PreAuthorize("hasPermission(#dummy, 'R') or hasPermission(#dummy, 'ADM')")
  @GetMapping("/find-all-by-id")
  default List<D> findAllById(@RequestParam List<I> ids, @Parameter(hidden = true) E dummy) {
    return svc().findAllByIdDto(ids);
  }

  @Operation(summary = "Поиск сущностей с поддержкой пагинации",
      description = "Поиск сущностей с поддержкой пагинации",
      parameters = {
          @Parameter(name = "page", description = "Порядковый номер нужной страницы", schema = @Schema(example = "0")),
          @Parameter(name = "size", description = "Количество элементов на странице", schema = @Schema(example = "20")),
          @Parameter(name = "sort", description = """
              Сортировка элементов на странице вида property1,property2(,asc|desc). Например ?sort=field1&sort=field2,asc
              """),
      }
  )
  @PreAuthorize("hasPermission(#dummy, 'R') or hasPermission(#dummy, 'ADM')")
  @GetMapping("/find-all")
  default Page<D> findAll(@Parameter(hidden = true) @ParameterObject @PageableDefault Pageable pageable, @Parameter(hidden = true) E dummy) {
    return svc().findAllDto(pageable);
  }
}
