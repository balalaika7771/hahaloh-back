package org.example.hahallohback.core.entity;

import base.abstractions.Identifiable;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Permission extends Identifiable<Permission> {

  private String name;  // Например, "W" или "ADM"
}