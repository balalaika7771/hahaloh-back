package org.example.hahallohback.core.repository;

import base.repository.JpaSpecificationExecutorRepository;
import java.util.Optional;
import org.example.hahallohback.core.entity.User;


public interface UserRepository extends JpaSpecificationExecutorRepository<User, Long> {
  Optional<User> findByUsername(String username);
}
