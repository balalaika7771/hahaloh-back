package org.example.hahallohback.security.config;

import lombok.AllArgsConstructor;
import org.example.hahallohback.security.CustomAuthenticationEntryPoint;
import org.example.hahallohback.security.filter.JwtAuthenticationFilter;
import org.example.hahallohback.security.provider.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  /**
   * Security filter chain for excluded URLs.
   * This chain permits all requests to the specified URLs without applying any security filters.
   */
  @Bean
  @Order(1) // Ensure this filter chain is evaluated first
  public SecurityFilterChain excludedSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/api/doc-ui/**", "/api/auth/**", "/api/oauth/*", "/v3/api-docs/**")
        .authorizeHttpRequests(authorize -> authorize
            .anyRequest().permitAll()
        )
        .csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }

  /**
   * Default security filter chain for all other URLs.
   * This chain applies security configurations, including JWT authentication.
   */
  @Bean
  @Order(2) // Evaluated after the excluded filter chain
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable) // Disable CSRF
        .authorizeHttpRequests(authorize -> authorize
            .anyRequest().authenticated() // All other requests require authentication
        )
        .exceptionHandling(exceptionHandling ->
            exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint) // Custom entry point for unauthorized access
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

    return http.build();
  }

  /**
   * Configure the AuthenticationManager with custom authentication provider.
   */
  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http, CustomAuthenticationProvider customAuthenticationProvider) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
    return authenticationManagerBuilder.build();
  }

  /**
   * Define the password encoder bean.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
