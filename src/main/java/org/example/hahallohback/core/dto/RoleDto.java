package org.example.hahallohback.core.dto;

import base.abstractions.IdentifiableDto;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.hahallohback.core.entity.Permission;


@AllArgsConstructor
@Getter
@Setter
public class RoleDto extends IdentifiableDto<RoleDto> {

  private String name;

  private Set<Permission> permissions;
}
