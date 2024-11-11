package org.example.hahallohback.core.transformer;

import base.transformer.Transformer;
import org.example.hahallohback.core.dto.RoleDto;
import org.example.hahallohback.core.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface RoleTransformer extends Transformer<RoleDto, Role> {

  @Mapping(target = "permissions", source = "permissions")
  RoleDto entityToDto(Role role);

  @Mapping(target = "permissions", source = "permissions")
  Role dtoToEntity(RoleDto roleDto);
}
