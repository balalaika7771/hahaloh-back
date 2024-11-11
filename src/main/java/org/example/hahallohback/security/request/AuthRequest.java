package org.example.hahallohback.security.request;

import org.example.hahallohback.core.dto.UserDto;


public record AuthRequest(String username, String password) {

  public UserDto toUserDto() {
    return new UserDto(username, password);
  }
}
