package org.example.hahallohback.OAuth.service;

import base.constants.entity.StateType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.example.hahallohback.OAuth.entity.UserState;
import org.example.hahallohback.core.repository.UserStateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class UserStateService {

  private final UserStateRepository userStateRepository;


  @Transactional
  public void storeState(Long userId, String state, StateType type) {
    UserState userState = new UserState();
    userState.setState(state);
    userState.setUserId(userId);
    userState.setStateType(type);
    userState.setCreatedAt(LocalDateTime.now());

    userStateRepository.save(userState);
  }

  @Transactional
  public Long retrieveUserIdByState(String state) {
    UserState userState = userStateRepository.findByState(state)
        .orElseThrow(() -> new RuntimeException("State not found!"));
    Long userId = userState.getUserId();
    userStateRepository.delete(userState); // Удаляем state после использования
    return userId;
  }

}
