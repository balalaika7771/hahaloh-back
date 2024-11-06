package base.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import lombok.NonNull;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;


@ExtensionMethod({ObjectExtensions.class})
@UtilityClass
public class ListExtensions {

  /**
   * Расплющить дерево
   *
   * @param roots          Руты
   * @param childExtractor Извлекатор детей 😈
   * @param accum          Аккумулятор
   * @param <T>            Тип элемента
   * @return Линейное дерев
   */
  public static <T> List<T> flatTree(@NonNull List<T> roots, @NonNull Function<T, List<T>> childExtractor, @NonNull List<T> accum) {
    roots.forEach(v -> {
      var child = childExtractor.apply(v);
      accum.add(v);
      if (child.hasValue() && !child.isEmpty()) flatTree(child, childExtractor, accum);
    });

    return accum;
  }

  /**
   * @see ListExtensions#flatTree(List, Function, List)
   */
  public static <T> List<T> flatTree(@NonNull List<T> roots, @NonNull Function<T, List<T>> childExtractor) {
    return flatTree(roots, childExtractor, new ArrayList<>());
  }
}
