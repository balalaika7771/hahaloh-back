package org.example.hahallohback.OAuth.dto;

import base.abstractions.IdentifiableDto;
import base.constants.entity.StateType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class UserStateDto extends IdentifiableDto<UserStateDto> {

  private String state;

  private StateType stateType;

  private Long userId;

}
