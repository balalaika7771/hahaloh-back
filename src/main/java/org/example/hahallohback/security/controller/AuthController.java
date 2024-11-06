package org.example.hahallohback.security.controller;

import base.controller.abstractions.BaseController;
import base.service.abstractions.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.hahallohback.core.dto.UserDto;
import org.example.hahallohback.core.entity.User;
import org.example.hahallohback.core.exception.InvalidCredentialsException;
import org.example.hahallohback.security.request.AuthRequest;
import org.example.hahallohback.security.service.JwtService;
import org.example.hahallohback.security.service.UserAuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "Authentication", description = "API для аутентификации и авторизации пользователей")
public class AuthController implements BaseController<UserDto, User> {

  private final UserAuthService userService;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  @Operation(summary = "Регистрация пользователя", description = "Создает нового пользователя с уникальным именем.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
      @ApiResponse(responseCode = "400", description = "Ошибка валидации запроса")
  })
  @PostMapping("/register")
  public UserDto register(
      @Parameter(description = "Данные пользователя для регистрации", required = true)
      @RequestBody User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return svc().t().entityToDto(userService.saveUser(user));
  }

  @Operation(summary = "Авторизация пользователя", description = "Авторизует пользователя и возвращает JWT-токен для последующего доступа.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешная авторизация, возвращен JWT-токен"),
      @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
  })
  @PostMapping("/login")
  public String login(
      @Parameter(description = "Учетные данные пользователя для входа", required = true)
      @RequestBody AuthRequest authRequest) {
    User user = userService.findByUsername(authRequest.getUsername());
    if (user != null && passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
      return jwtService.generateToken(user);
    }
    throw new InvalidCredentialsException("Invalid credentials", 401);
  }

  @Operation(summary = "Получение информации о текущем пользователе", description = "Возвращает данные текущего авторизованного пользователя.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Информация о пользователе возвращена"),
      @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
  })
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