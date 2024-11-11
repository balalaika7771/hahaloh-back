package org.example.hahallohback.core.dto;

import base.abstractions.IdentifiableDto;
import base.constants.entity.AccessLevel;
import base.constants.entity.EntityName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class PermissionDto extends IdentifiableDto<PermissionDto> {

  private EntityName entityName;

  private AccessLevel accessLevel;
}
