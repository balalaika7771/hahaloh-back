package org.example.hahallohback.core.service;

import base.repository.JpaSpecificationExecutorRepository;
import base.service.jpa.CrudJpaService;
import base.transformer.Transformer;
import lombok.AllArgsConstructor;
import org.example.hahallohback.core.dto.UserDto;
import org.example.hahallohback.core.entity.User;
import org.example.hahallohback.core.repository.UserRepository;
import org.example.hahallohback.core.transformer.UserTransformer;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserService implements CrudJpaService<UserDto, User, Long> {

  private final UserRepository userRepository;

  private final UserTransformer userTransformer;

  @Override
  public JpaSpecificationExecutorRepository<User, Long> repo() {
    return userRepository;
  }

  @Override
  public Transformer<UserDto, User> t() {
    return userTransformer;
  }

  @Override
  public User enrichEntity(User entity) {
    if (entity.getId() != null && entity.getPassword() == null) {
      userRepository.findById(entity.getId())
          .ifPresent(existingUser -> entity.setPassword(existingUser.getPassword()));
    }
    return entity;
  }

}
