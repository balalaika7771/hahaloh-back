package org.example.hahallohback.OAuth.transformer;

import base.transformer.Transformer;
import org.example.hahallohback.OAuth.dto.UserTokenDto;
import org.example.hahallohback.OAuth.entity.UserToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING)
public interface UserTokenTransformer extends Transformer<UserTokenDto, UserToken> {


  @Mapping(source = "id", target = "id")
  UserTokenDto entityToDto(UserToken product);

  @Mapping(target = "refreshToken", ignore = true)
  @Mapping(source = "id", target = "id")
  UserToken dtoToEntity(UserTokenDto productDto);
}
