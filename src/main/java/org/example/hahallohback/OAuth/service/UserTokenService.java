package org.example.hahallohback.OAuth.service;

import base.constants.entity.TokenType;
import base.service.abstractions.BaseJpaService;
import base.transformer.Transformer;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.hahallohback.OAuth.dto.UserTokenDto;
import org.example.hahallohback.OAuth.entity.UserToken;
import org.example.hahallohback.OAuth.repository.UserTokenRepository;
import org.example.hahallohback.OAuth.transformer.UserTokenTransformer;
import org.example.hahallohback.core.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserTokenService implements BaseJpaService<UserTokenDto, UserToken, Long> {

  private final UserTokenRepository userTokenRepository;
  private final WebClient.Builder webClientBuilder;
  private final UserService userService;
  private final UserTokenTransformer userTokenTransformer;

  @Value("${hh.client-id}")
  private String clientId;

  @Value("${hh.client-secret}")
  private String clientSecret;

  @Value("${hh.redirect-uri}")
  private String redirectUri;

  private static final String HH_TOKEN_URL = "https://hh.ru/oauth/token";



  @Transactional
  public boolean exchangeCodeForTokens(Long userId, String code) {
    WebClient webClient = webClientBuilder.build();

    userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));

    var tokenResponse = webClient.post()
        .uri(HH_TOKEN_URL)
        .header("Content-Type", "application/x-www-form-urlencoded")
        .bodyValue("grant_type=authorization_code" +
            "&client_id=" + clientId +
            "&client_secret=" + clientSecret +
            "&code=" + code +
            "&redirect_uri=" + redirectUri)
        .retrieve()
        .bodyToMono(Map.class)
        .block();

    if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
      return false;
    }

    String accessToken = (String) tokenResponse.get("access_token");
    String refreshToken = (String) tokenResponse.get("refresh_token");
    int expiresIn = (Integer) tokenResponse.get("expires_in");


    saveTokens(userId, accessToken, refreshToken, expiresIn);
    return true;
  }

  @Transactional
  public void saveTokens(Long userId, String accessToken, String refreshToken, int expiresInSeconds) {
    UserToken userToken = userTokenRepository.findByUserIdAndTokenType(userId, TokenType.HH)
        .orElse(new UserToken());

    userToken.setUserId(userId);
    userToken.setTokenType(TokenType.HH);
    userToken.setAccessToken(accessToken);
    userToken.setRefreshToken(refreshToken);
    userToken.setExpiresAt(LocalDateTime.now().plusSeconds(expiresInSeconds));

    userTokenRepository.save(userToken);
  }

  @Override
  public JpaRepository<UserToken, Long> repo() {
    return userTokenRepository;
  }

  @Override
  public Transformer<UserTokenDto, UserToken> t() {
    return userTokenTransformer;
  }
}
