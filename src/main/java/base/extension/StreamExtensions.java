package base.extension;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import reactor.util.function.Tuple2;


/**
 * @author Ivan Zhendorenko
 */
@UtilityClass
public class StreamExtensions {

  /**
   * Кортеж длинной 2 в мапу
   *
   * @param stream Стрим кортежей
   * @param <K>    Тип ключа
   * @param <V>    Тип значения
   * @return Мапа. Первое значение кортежа - ключ, Второе значение кортежа - значение
   */
  public static <K, V> Map<K, V> toMap(Stream<Tuple2<K, V>> stream) {
    return stream.collect(Collectors.toMap(Tuple2::getT1, Tuple2::getT2));
  }

  /**
   * Обёртка для {@link  Collectors#toMap(Function, Function) метода}
   */
  public static <K, V, E> Map<K, V> toMap(Stream<E> stream,
                                          Function<? super E, ? extends K> keyMapper,
                                          Function<? super E, ? extends V> valueMapper) {
    return stream.collect(Collectors.toMap(keyMapper, valueMapper));
  }
}
