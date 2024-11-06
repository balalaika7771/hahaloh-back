package base.extension;

import lombok.experimental.UtilityClass;


/**
 * Общие методы расширения
 *
 * @author Ivan Zhendorenko
 */
@UtilityClass
public class ObjectExtensions {

  public static boolean hasValue(Object o) {
    return o != null;
  }

  public static boolean isNull(Object o) {
    return o == null;
  }
}
