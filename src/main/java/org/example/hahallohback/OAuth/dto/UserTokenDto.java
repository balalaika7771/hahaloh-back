package org.example.hahallohback.OAuth.dto;


import base.abstractions.IdentifiableDto;
import base.constants.entity.TokenType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class UserTokenDto extends IdentifiableDto<UserTokenDto> {

  private Long userId;

  private TokenType tokenType;

  private String accessToken;

  private LocalDateTime expiresAt; // Дата и время истечения `access_token`

  // Метод для проверки, истек ли `access_token`
  public boolean isAccessTokenExpired() {
    return LocalDateTime.now().isAfter(expiresAt);
  }
}
