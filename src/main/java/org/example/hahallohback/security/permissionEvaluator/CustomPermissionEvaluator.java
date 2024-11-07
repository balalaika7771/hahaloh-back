package org.example.hahallohback.security.permissionEvaluator;

import base.constants.entity.AccessLevel;
import base.constants.entity.EntityName;
import java.io.Serializable;
import org.example.hahallohback.security.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

  @Autowired
  private PermissionService permissionService;

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
    if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails user)) {
      return false;
    }

    if (targetDomainObject == null || !(permission instanceof String)) {
      return false;
    }

    // Получаем имя класса и преобразуем его в EntityName
    String className = targetDomainObject.getClass().getSimpleName();
    EntityName entityName;
    try {
      entityName = EntityName.from(className);
    } catch (IllegalArgumentException e) {
      return false; // Если имя класса не соответствует ни одному EntityName
    }

    // Преобразуем строковое разрешение в AccessLevel
    AccessLevel accessLevel;
    try {
      accessLevel = AccessLevel.valueOf((String) permission);
    } catch (IllegalArgumentException e) {
      return false; // Если передан неверный уровень доступа
    }

    // Проверяем, имеет ли пользователь разрешение на указанный уровень доступа для данной сущности
    return permissionService.hasPermission(user, entityName, accessLevel);
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
    // Метод не используется, так как проверка осуществляется по типу сущности, а не по ID
    return false;
  }
}
