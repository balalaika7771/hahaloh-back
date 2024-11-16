package base.abstractions;

import java.time.LocalDateTime;


public interface Token {

  String getAccessToken();

  String getRefreshToken();

  LocalDateTime getExpiresAt(); // Дата и время истечения `AccessToken'

}
