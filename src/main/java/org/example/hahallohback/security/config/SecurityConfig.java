package org.example.hahallohback.security.config;

import lombok.AllArgsConstructor;
import org.example.hahallohback.security.CustomAuthenticationEntryPoint;
import org.example.hahallohback.security.provider.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable) // Отключаем CSRF с использованием нового синтаксиса
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/v3/api-docs/**").permitAll()// Разрешаем доступ к эндпоинтам авторизации
            .anyRequest().authenticated() // Остальные запросы требуют авторизации
        )
        .exceptionHandling(exceptionHandling ->
            exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint) // Указываем кастомный AuthenticationEntryPoint
        );

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http, CustomAuthenticationProvider customAuthenticationProvider) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
    return authenticationManagerBuilder.build();
  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}


