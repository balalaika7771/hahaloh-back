package org.example.hahallohback.OAuth.service;

import base.abstractions.Token;
import base.constants.entity.StateType;
import base.model.SimpleToken;
import base.service.abstractions.OAuthService;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.hahallohback.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@RequiredArgsConstructor
public class OAuthHHService implements OAuthService {

  @Value("${hh.client-id}")
  private String clientId;

  @Value("${hh.redirect-uri}")
  private String redirectUri;

  @Value("${hh.client-secret}")
  private String clientSecret;

  private final UserStateService userStateService;

  private final UserTokenService userTokenService;

  private final WebClient.Builder webClientBuilder;

  private final UserService userService;

  @Autowired
  private ApplicationContext applicationContext;

  public OAuthHHService self() {
    return applicationContext.getBean(OAuthHHService.class);
  }


  private static final String HH_TOKEN_URL = "https://hh.ru/oauth/token";

  public Token exchangeCodeForTokens(Long userId, String code) {
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
      throw new RuntimeException("tokenResponse == null || !tokenResponse.containsKey(\"access_token\")");
    }

    String accessToken = (String) tokenResponse.get("access_token");
    String refreshToken = (String) tokenResponse.get("refresh_token");
    LocalDateTime expiresIn = LocalDateTime.now().plusSeconds((Integer) tokenResponse.get("expires_in"));

    return new SimpleToken(accessToken, refreshToken, expiresIn);
  }

  @Override
  public String registerStateAndGetAuthUrl(Long userId) {

    String state = UUID.randomUUID().toString();
    userStateService.storeState(userId, state, self().stateType());

    // Формируем URL для авторизации
    return "https://hh.ru/oauth/authorize"
        + "?response_type=code&client_id=" + clientId
        + "&redirect_uri=" + redirectUri
        + "&state=" + state;
  }

  @Override
  public void saveToken(String code, String state) {
    Long userId = userStateService.retrieveUserIdByState(state);
    Token token = self().exchangeCodeForTokens(userId, code);
    userTokenService.saveTokens(userId, token);
  }

  @Override
  public StateType stateType() {
    return StateType.OAUTH_HH;
  }
}
