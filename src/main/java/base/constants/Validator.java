package base.constants;

/**
 * @author Ivan Zhendorenko
 */
public class Validator {

  /**
   * Название рутового элемента в валидационной структуре
   */
  public static final String ROOT = "root";

  /**
   * Тип операции, над которой необходимо выполнить валидацию
   */
  public enum ValidationOperationType {

    /**
     * Сохранение
     */
    SAVE,

    /**
     * Обновление
     */
    UPDATE
  }
}
