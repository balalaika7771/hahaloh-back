package org.example.hahallohback.core.transformer;

import base.transformer.Transformer;
import org.example.hahallohback.core.dto.UserDto;
import org.example.hahallohback.core.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING)
public interface UserTransformer extends Transformer<UserDto, User> {

  @Mapping(target = "password", ignore = true)
  @Mapping(source = "id", target = "id")
  UserDto entityToDto(User product);

  @Mapping(source = "id", target = "id")
  User dtoToEntity(UserDto productDto);
}
