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
import org.example.hahallohback.security.response.AuthResponse;
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
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "Authentication", description = "API для аутентификации и авторизации пользователей")
public class AuthController implements BaseController<UserDto, User> {

  private final UserAuthService userService;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  @Operation(summary = "Регистрация пользователя", description = "Создает нового пользователя с уникальным именем и возвращает JWT-токен.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован, возвращен JWT-токен"),
      @ApiResponse(responseCode = "400", description = "Ошибка валидации запроса")
  })
  @PostMapping("/register")
  public AuthResponse register(
      @Parameter(description = "Данные пользователя для регистрации", required = true)
      @RequestBody UserDto userDto) {

    var user = userService.findByUsername(userDto.getUsername());
    if (user.isPresent()) {
      throw new RuntimeException("Name exists");
    }

    // Кодируем пароль и сохраняем пользователя
    userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
    User savedUser = userService.saveUser(svc().enrichEntity(svc().t().dtoToEntity(userDto)));

    // Генерируем JWT-токен для нового пользователя
    String token = jwtService.generateToken(savedUser);
    return new AuthResponse(token, savedUser.getUsername());
  }

  @Operation(summary = "Авторизация пользователя", description = "Авторизует пользователя и возвращает JWT-токен для последующего доступа.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешная авторизация, возвращен JWT-токен"),
      @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
  })
  @PostMapping("/login")
  public AuthResponse login(
      @Parameter(description = "Учетные данные пользователя для входа", required = true)
      @RequestBody UserDto userDto) {

    User user = userService.getByUsername(userDto.getUsername());
    if (user != null && passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
      // Генерируем JWT-токен для авторизованного пользователя
      String token = jwtService.generateToken(user);
      return new AuthResponse(token, user.getUsername());
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
    return userService.findByUsernameDto(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
  }

  @Override
  public BaseService<UserDto, User> svc() {
    return userService;
  }
}

