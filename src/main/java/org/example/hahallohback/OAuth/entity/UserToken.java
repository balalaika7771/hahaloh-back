package org.example.hahallohback.OAuth.entity;


import base.abstractions.Identifiable;
import base.constants.entity.TokenType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_tokens")
public class UserToken extends Identifiable<UserToken> {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "token_type", nullable = false)
  private TokenType tokenType;

  @Column(name = "access_token", nullable = false)
  private String accessToken;

  @Column(name = "refresh_token", nullable = false)
  private String refreshToken;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt; // Дата и время истечения `access_token`

  // Метод для проверки, истек ли `access_token`
  public boolean isAccessTokenExpired() {
    return LocalDateTime.now().isAfter(expiresAt);
  }
}
