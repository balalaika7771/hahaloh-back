package base.constants;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;


/**
 * Константы, относящиеся к спрингу
 */
@NoArgsConstructor(access = PRIVATE)
public final class Spring {

  /**
   * Профили
   */
  @NoArgsConstructor(access = PRIVATE)
  public static final class Profile {

    /**
     * Профиль с отключенными бинами редиса
     */
    public static final String NO_REDIS = "noredis";

    /**
     * Профиль с отключенными бинами vault
     */
    public static final String NO_VAULT = "novault";
  }
}
