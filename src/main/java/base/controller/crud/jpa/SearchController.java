package base.controller.crud.jpa;

import base.controller.abstractions.BaseController;
import base.service.jpa.ReadJpaService;
import base.util.Data;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.ExtensionMethod;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Контроллер для чтения (поиска)
 *
 * @param <D> Тип дто
 * @param <E> Тип сущности
 * @param <I> Тип идентификатора
 * @author Ivan Zhendorenko
 */
@ExtensionMethod({Data.class})
public interface SearchController<D, E, I> extends BaseController<D, E> {

  /**
   * Сервис, используемый контроллером
   *
   * @return Сервис
   */
  @Override
  ReadJpaService<D, E, I> svc();

  @SuppressWarnings("unchecked")
  @Operation(summary = "Поиск сущностей по фильтрам",
      description = """
          Поиск сущностей по фильтрам.
                              
          Логическая операция, выполняемая между условиями - AND.
                              
          Список поддерживаемых операций:
                              
              - Эквиваленция: ':'
              
              - Отрицание: '!'
              
              - Больше, чем: '>'
              
              - Меньше, чем: '<'
              
              - Соответствует шаблону: '~'
                              
          Примеры:
              - ?search=name:Joe,age>14.1,lastName!Doe,email~girl
              - ?search=departments.title~Отдел,users.id:0
              - ?search=startExaminationDate>2023-07-19,startExaminationDate<2023-07-21
          """,
      parameters = {
          @Parameter(name = "search", description = "Строка с условиями для фильтрации", required = true),
          @Parameter(name = "isDistinct", description = "Вывод только уникальных значений"),
          @Parameter(name = "page", description = "Порядковый номер нужной страницы", schema = @Schema(example = "0")),
          @Parameter(name = "size", description = "Количество элементов на странице", schema = @Schema(example = "20")),
          @Parameter(name = "sort", description = """
              Сортировка элементов на странице вида property1,property2(,asc|desc). Например ?sort=field1&sort=field2,asc
              """),
      }

  )
  @PreAuthorize("hasPermission(#dummy, 'R') or hasPermission(#dummy, 'ADM')")
  @GetMapping("/search")
  default Page<D> search(@RequestParam(value = "search") String search,
                         @RequestParam(value = "isDistinct", required = false, defaultValue = "false") boolean isDistinct,
                         @Parameter(hidden = true) @ParameterObject @PageableDefault Pageable pageable,
                         @Parameter(hidden = true) E dummy) {
    var spec = search.searchToSpec(svc().searchPathReplacer(), isDistinct);
    return svc().findAllDto(spec, pageable);
  }
}
