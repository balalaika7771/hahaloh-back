package base.constants.entity;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;


/**
 * Названия сущностей в виде строковых констант, чтобы можно было использовать в аннотациях
 *
 * @author Ivan Zhendorenko
 */
@NoArgsConstructor(access = PRIVATE)
public class EntityNames {

  public static final String USER = "User";

  public static final String PERMISSION = "Permission";

  public static final String ROLE = "Role";

}