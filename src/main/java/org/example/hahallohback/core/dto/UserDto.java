package org.example.hahallohback.core.dto;

import base.abstractions.IdentifiableDto;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.example.hahallohback.core.entity.Role;


@Getter
@Setter
public class UserDto extends IdentifiableDto<UserDto> {

  @NotNull
  private String username;

  @NotNull
  private String password;

  public UserDto(String username, String password) {
    this.username = username;
    this.password = password;
  }

  private Set<Role> roles;
}
