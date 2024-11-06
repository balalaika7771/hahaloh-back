package base.extension;

import java.util.List;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import static java.util.Arrays.stream;


/**
 * @author Ivan Zhendorenko
 */
@UtilityClass
public class ArrayExtensions {

  public static <T> List<T> toList(@NonNull T[] array) {
    return stream(array).toList();
  }
}
