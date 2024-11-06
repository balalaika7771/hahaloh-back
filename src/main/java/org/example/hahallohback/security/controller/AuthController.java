package org.example.hahallohback.security.controller;

import base.controller.abstractions.BaseController;
import base.service.abstractions.BaseService;
import lombok.AllArgsConstructor;
import org.example.hahallohback.core.dto.UserDto;
import org.example.hahallohback.core.entity.User;
import org.example.hahallohback.core.exception.InvalidCredentialsException;
import org.example.hahallohback.security.request.AuthRequest;
import org.example.hahallohback.security.service.JwtService;
import org.example.hahallohback.security.service.UserAuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController implements BaseController<UserDto, User> {

  private final UserAuthService userService;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/register")
  public UserDto register(@RequestBody User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return svc().t().entityToDto(userService.saveUser(user));
  }

  @PostMapping("/login")
  public String login(@RequestBody AuthRequest authRequest) {
    User user = userService.findByUsername(authRequest.getUsername());
    if (user != null && passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
      return jwtService.generateToken(user);
    }
    throw new InvalidCredentialsException("Invalid credentials", 401);
  }

  @GetMapping("/who_am_i")
  public UserDto whoAmI(Authentication authentication) {
    String username = authentication.getName();
    return userService.findByUsernameDto(username);
  }

  @Override
  public BaseService<UserDto, User> svc() {
    return userService;
  }
}
