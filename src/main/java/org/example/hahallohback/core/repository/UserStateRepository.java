package org.example.hahallohback.core.repository;


import base.constants.entity.StateType;
import java.util.Optional;
import org.example.hahallohback.OAuth.entity.UserState;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserStateRepository extends JpaRepository<UserState, Long> {

  Optional<UserState> findByState(String state);

  Optional<UserState> findByUserIdAndStateType(Long userId, StateType type);
  void deleteByState(String state);
}

