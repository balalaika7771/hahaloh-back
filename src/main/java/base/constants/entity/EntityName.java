package base.constants.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * Названия всех сущностей в системе
 *
 * @author Ivan Zhendorenko
 */
@Getter
@RequiredArgsConstructor
public enum EntityName {
  USER(EntityNames.USER, "user"),
  ROLE(EntityNames.ROLE, "role"),
  PERMISSION(EntityNames.PERMISSION, "permission");

  private final String name;

  private final String apiPath;

  EntityName(String name) {
    this.name = name;
    this.apiPath = "";
  }

  @JsonCreator
  public static EntityName from(String name) {
    for (EntityName entityName : EntityName.values())
      if (entityName.name.equalsIgnoreCase(name))
        return entityName;

    throw new IllegalArgumentException("Invalid EntityName: " + name);
  }

  @JsonValue
  public String toJsonString() {
    return name;
  }
}

