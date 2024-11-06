package base.controller.crudversioning;

import base.abstractions.Versionable;
import base.controller.abstractions.BaseController;
import base.search.SpecificationBuilder;
import base.service.crudversioning.ReadVersioningService;
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

import static java.util.regex.Pattern.compile;


/**
 * Контроллер для чтения версионных сущностей
 *
 * @param <D> Тип дто
 * @param <E> Тип сущности
 * @param <I> Тип идентификатора
 * @author Ivan Zhendorenko
 */
public interface ReadVersioningController<D extends Versionable<D>, E extends Versionable<E>, I> extends BaseController<D, E> {

  /**
   * Сервис, используемый контроллером
   *
   * @return Сервис
   */
  @Override
  ReadVersioningService<D, E, I> svc();

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

  @Operation(summary = "Поиск актуальной версии",
      description = "Поиск актуальной версии в рамках одной ветки версий",
      parameters = @Parameter(name = "branchId", description = "Идентификатор ветки версий")

  )
  @PreAuthorize("hasPermission(#dummy, 'R') or hasPermission(#dummy, 'ADM')")
  @GetMapping("/find-actual-by-branch-id/{branchId}")
  default Optional<D> findActualByBranchId(@PathVariable Long branchId, @Parameter(hidden = true) E dummy) {
    return svc().findActualByBranchIdDto(branchId);
  }

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
                         @Parameter(hidden = true) @ParameterObject @PageableDefault Pageable pageable, @Parameter(hidden = true) E dummy) {

    var patternStr = "(\\S+?)([:<>~!])(([\\w\\s\\d]|[а-яёА-ЯЁ]|[\\\\/'№\\-@\"])+?),";
    var pattern = compile(patternStr);
    var matcher = pattern.matcher(search + ",");

    var builder = new SpecificationBuilder<E>(svc().searchPathReplacer());
    while (matcher.find())
      builder = builder.with(matcher.group(1), matcher.group(2), matcher.group(3), false, isDistinct);

    var spec = builder.build();

    return svc().findAllDto(spec, pageable);
  }
}
