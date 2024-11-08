package org.example.hahallohback.security.service;

import base.service.abstractions.BaseJpaService;
import base.transformer.Transformer;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.example.hahallohback.core.dto.UserDto;
import org.example.hahallohback.core.entity.User;
import org.example.hahallohback.core.repository.UserRepository;
import org.example.hahallohback.core.transformer.UserTransformer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAuthService implements UserDetailsService, BaseJpaService<UserDto, User, Long> {

  private final UserRepository userRepository;
  private final UserTransformer userTransformer;

  public User saveUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<UserDto> findByUsernameDto(String username) {
    return t().entityToDto(userRepository.findByUsername(username));
  }

  public User getByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
  }

  public UserDto getByUsernameDto(String username) {
    return t().entityToDto(userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username)));
  }

  @Override
  public JpaRepository<User, Long> repo() {
    return userRepository;
  }

  @Override
  public Transformer<UserDto, User> t() {
    return userTransformer;
  }
}

