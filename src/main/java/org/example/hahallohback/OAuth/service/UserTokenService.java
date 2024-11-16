package org.example.hahallohback.OAuth.service;

import base.abstractions.Token;
import base.constants.entity.TokenType;
import base.service.abstractions.BaseJpaService;
import base.transformer.Transformer;
import lombok.RequiredArgsConstructor;
import org.example.hahallohback.OAuth.dto.UserTokenDto;
import org.example.hahallohback.OAuth.entity.UserToken;
import org.example.hahallohback.OAuth.repository.UserTokenRepository;
import org.example.hahallohback.OAuth.transformer.UserTokenTransformer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserTokenService implements BaseJpaService<UserTokenDto, UserToken, Long> {

  private final UserTokenRepository userTokenRepository;


  private final UserTokenTransformer userTokenTransformer;







  @Transactional
  public void saveTokens(Long userId, Token token) {
    UserToken userToken = userTokenRepository.findByUserIdAndTokenType(userId, TokenType.HH)
        .orElse(new UserToken());

    userToken.setUserId(userId);
    userToken.setTokenType(TokenType.HH);
    userToken.setAccessToken(token.getAccessToken());
    userToken.setRefreshToken(token.getRefreshToken());
    userToken.setExpiresAt(token.getExpiresAt());

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
