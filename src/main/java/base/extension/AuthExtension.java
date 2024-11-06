package base.extension;

import base.model.JwtAuthentication;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;


/**
 * @author Ivan Zhendorenko
 */
@UtilityClass
public class AuthExtension {

  public static JwtAuthentication asJwt(@NotNull Authentication auth) {
    return (JwtAuthentication) auth;
  }
}
