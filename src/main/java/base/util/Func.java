package base.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;


/**
 * Утилиты для работы с функциональными интерфейсами
 *
 * @author Ivan Zhendorenko
 */
@UtilityClass
@Slf4j
public class Func {

  /**
   * Пустой консьюмер
   */
  @SuppressWarnings("rawtypes")
  public static final Consumer NONE = d -> {
  };

  /**
   * Предикат, позволяющий фильтровать значение по функции извлекающей ключ
   *
   * @param keyExtractor Функция, извлекающая ключ
   * @param <T>          Тип входного параметра
   * @return True, если это первое значение с подобным ключом
   */
  public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
    var map = new ConcurrentHashMap<Object, Boolean>();
    return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
  }

  /**
   * Foreach с счётчиком
   *
   * @param source   Исходная коллекция
   * @param consumer Функция-потребитель
   * @param <T>      Тип элемента в коллекции
   */
  public static <T> void forEachWithCounter(Iterable<T> source, BiConsumer<Integer, T> consumer) {
    if (source == null)
      return;

    int i = 0;
    for (T item : source) {
      consumer.accept(i, item);
      i++;
    }
  }

  /**
   * Exception-safe выполнение функции
   * <p><b></b></p>
   * Исключения пишутся в лог
   *
   * @param func Функция
   * @return Вылетело ли исключение
   */
  public static boolean saveExec(Supplier<?> func) {
    var result = false;
    try {
      func.get();
      result = true;
    } catch (Exception ex) {
      log.atError().setCause(ex).log(ex::getMessage);
    }

    return result;
  }


  /**
   * Exception-safe выполнение функции
   * <p><b></b></p>
   * Исключения пишутся в лог
   *
   * @param func Функция
   * @return Вылетело ли исключение
   */
  public static boolean saveExec(Runnable func) {
    return saveExec(() -> {
      func.run();
      return null;
    });
  }
}
