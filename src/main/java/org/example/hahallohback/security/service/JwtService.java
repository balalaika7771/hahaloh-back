package org.example.hahallohback.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

  // Секретный ключ для подписания токенов (замените на свой уникальный ключ)
  private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  // Метод для генерации токена
  public String generateToken(UserDetails userDetails) {
    return Jwts.builder()
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Токен на 10 часов
        .signWith(SECRET_KEY)
        .compact();
  }

  // Извлекаем имя пользователя из токена
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  // Проверяем валидность токена
  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  // Проверяем, истек ли токен
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  // Извлекаем дату истечения токена
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  // Извлекаем определенное требование (claim) из токена
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  // Извлекаем все требования (claims) из токена
  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(SECRET_KEY)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}

