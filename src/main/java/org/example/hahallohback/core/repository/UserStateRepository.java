package org.example.hahallohback.core.repository;


import java.util.Optional;
import org.example.hahallohback.core.entity.UserState;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserStateRepository extends JpaRepository<UserState, Long> {

  Optional<UserState> findByState(String state);

  void deleteByState(String state);
}

