package org.example.hahallohback.core.entity;

import base.abstractions.Identifiable;
import base.constants.entity.AccessLevel;
import base.constants.entity.EntityName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "permission")
public class Permission extends Identifiable<Permission> {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EntityName entityName;  // Используем EntityName вместо EntityType

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AccessLevel accessLevel;  // Используем AccessLevel для уровня доступа
}
