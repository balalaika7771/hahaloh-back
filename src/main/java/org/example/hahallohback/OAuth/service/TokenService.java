package org.example.hahallohback.OAuth.service;

import base.constants.entity.StateType;
import base.constants.entity.TokenType;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.hahallohback.OAuth.entity.UserToken;
import org.example.hahallohback.OAuth.repository.UserTokenRepository;
import org.example.hahallohback.core.entity.User;
import org.example.hahallohback.core.entity.UserState;
import org.example.hahallohback.core.repository.UserStateRepository;
import org.example.hahallohback.core.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@RequiredArgsConstructor
public class TokenService {

  private final UserTokenRepository userTokenRepository;
  private final UserStateRepository userStateRepository;
  private final WebClient.Builder webClientBuilder;
  private final UserService userService;

  @Value("${hh.client-id}")
  private String clientId;

  @Value("${hh.client-secret}")
  private String clientSecret;

  @Value("${hh.redirect-uri}")
  private String redirectUri;

  private static final String HH_TOKEN_URL = "https://hh.ru/oauth/token";

  public void storeState(Long userId, String state) {
    UserState userState = new UserState();
    userState.setState(state);
    userState.setUserId(userId);
    userState.setStateType(StateType.OAUTH_HH);
    userState.setCreatedAt(LocalDateTime.now());

    userStateRepository.save(userState);
  }

  public Long retrieveUserIdByState(String state) {
    UserState userState = userStateRepository.findByState(state)
        .orElseThrow(() -> new RuntimeException("Состояние не найдено!"));
    Long userId = userState.getUserId();
    userStateRepository.deleteByState(state); // Удаляем state после использования
    return userId;
  }

  public boolean exchangeCodeForTokens(Long userId, String code) {
    WebClient webClient = webClientBuilder.build();

    String requestBody = "grant_type=authorization_code"
        + "&client_id=" + clientId
        + "&client_secret=" + clientSecret
        + "&code=" + code
        + "&redirect_uri=" + redirectUri;

    Map<String, Object> tokenResponse = webClient.post()
        .uri(HH_TOKEN_URL)
        .header("Content-Type", "application/x-www-form-urlencoded")
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(Map.class)
        .block();

    if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
      return false;
    }

    String accessToken = (String) tokenResponse.get("access_token");
    String refreshToken = (String) tokenResponse.get("refresh_token");
    int expiresIn = (Integer) tokenResponse.get("expires_in");

    User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("Пользователь не найден!"));

    saveTokens(user, accessToken, refreshToken, expiresIn);
    return true;
  }

  public void saveTokens(User user, String accessToken, String refreshToken, int expiresInSeconds) {
    UserToken userToken = userTokenRepository.findByUserAndTokenType(user, TokenType.HH)
        .orElse(new UserToken());

    userToken.setUser(user);
    userToken.setTokenType(TokenType.HH);
    userToken.setAccessToken(accessToken);
    userToken.setRefreshToken(refreshToken);
    userToken.setExpiresAt(LocalDateTime.now().plusSeconds(expiresInSeconds));

    userTokenRepository.save(userToken);
  }
}
