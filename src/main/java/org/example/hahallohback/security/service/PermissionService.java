package org.example.hahallohback.security.service;

import base.constants.entity.AccessLevel;
import base.constants.entity.EntityName;
import org.example.hahallohback.core.entity.User;
import org.example.hahallohback.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class PermissionService {

  @Autowired
  private UserRepository userRepository;

  public boolean hasPermission(UserDetails userDetails, EntityName entityName, AccessLevel accessLevel) {
    // Находим пользователя по его имени
    User user = userRepository.findByUsername(userDetails.getUsername())
        .orElse(null);

    if (user == null) {
      return false;
    }

    // Проходим по ролям пользователя и проверяем права
    return user.getRoles().stream()
        .flatMap(role -> role.getPermissions().stream())
        .anyMatch(permission -> permission.getEntityName() == entityName && permission.getAccessLevel() == accessLevel);
  }
}
