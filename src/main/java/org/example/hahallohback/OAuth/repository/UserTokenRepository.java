package org.example.hahallohback.OAuth.repository;


import base.constants.entity.TokenType;
import java.util.Optional;
import org.example.hahallohback.OAuth.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

  Optional<UserToken> findByUserIdAndTokenType(Long userId, TokenType tokenType);

}
