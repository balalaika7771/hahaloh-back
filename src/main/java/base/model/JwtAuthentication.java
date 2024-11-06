package base.model;

import java.util.Collection;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


/**
 * @author Ivan Zhendorenko
 */
@Getter
@Setter
@Accessors(chain = true)
public class JwtAuthentication implements Authentication {

  private boolean authenticated;

  private String login;

  private String name;

  private Set<SimpleGrantedAuthority> roles;

  private String token;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getDetails() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return login;
  }

  @Override
  public boolean isAuthenticated() {
    return authenticated;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    this.authenticated = isAuthenticated;
  }

  @Override
  public String getName() {
    return name;
  }

  public String getAsBearer() {
    return "Bearer %s".formatted(getToken());
  }
}
