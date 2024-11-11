package org.example.hahallohback.core.transformer;

import base.transformer.Transformer;
import org.example.hahallohback.core.dto.PermissionDto;
import org.example.hahallohback.core.entity.Permission;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING)
public interface PermissionTransformer extends Transformer<PermissionDto, Permission> {

  PermissionDto entityToDto(Permission role);

  Permission dtoToEntity(PermissionDto roleDto);

}
