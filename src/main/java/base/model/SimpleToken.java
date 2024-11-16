package base.model;

import base.abstractions.Token;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class SimpleToken implements Token {

  private final String accessToken;

  private final String refreshToken;

  private final LocalDateTime expiresAt;
}
