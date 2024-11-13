package org.example.hahallohback.OAuth.transformer;

import base.transformer.Transformer;
import org.example.hahallohback.OAuth.dto.UserStateDto;
import org.example.hahallohback.OAuth.entity.UserState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING)
public interface UserStateTransformer extends Transformer<UserStateDto, UserState> {


  @Mapping(source = "id", target = "id")
  UserStateDto entityToDto(UserState product);

  @Mapping(target = "createdAt", ignore = true)
  @Mapping(source = "id", target = "id")
  UserState dtoToEntity(UserStateDto productDto);
}
