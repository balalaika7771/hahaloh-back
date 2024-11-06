package org.example.hahallohback.core.dto;

import base.abstractions.IdentifiableDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class UserDto extends IdentifiableDto<UserDto> {

  @NotNull
  private String username;
}
