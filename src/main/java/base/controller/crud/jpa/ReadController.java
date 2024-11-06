package base.controller.crud.jpa;

import base.controller.abstractions.BaseController;
import base.service.jpa.ReadJpaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface ReadController<D, E, I> extends BaseController<D, E> {

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
  @GetMapping("/find-by-id/{id}")
  default ResponseEntity<D> findById(@PathVariable I id) {
    return svc().findByIdDto(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @Operation(summary = "Поиск",
      description = "Поиск сущностей по идентификаторам",
      parameters = @Parameter(name = "ids", description = "Идентификаторы искомых сущностей в виде id1,id2,id3", required = true)
  )
  @GetMapping("/find-all-by-id")
  default ResponseEntity<List<D>> findAllById(@RequestParam List<I> ids) {
    List<D> results = svc().findAllByIdDto(ids);
    if (results.isEmpty()) {
      return ResponseEntity.noContent().build();  // Если список пуст, возвращаем 204 No Content
    }
    return ResponseEntity.ok(results);  // Если найдены, возвращаем 200 OK
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
  @GetMapping("/find-all")
  default Page<D> findAll(@Parameter(hidden = true) @ParameterObject @PageableDefault Pageable pageable) {
    return svc().findAllDto(pageable);
  }
}
