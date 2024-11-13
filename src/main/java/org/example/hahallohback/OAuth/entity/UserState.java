package org.example.hahallohback.OAuth.entity;

import base.abstractions.Identifiable;
import base.constants.entity.StateType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_state")
public class UserState extends Identifiable<UserState> {

  @Column(name = "state", nullable = false, unique = true)
  private String state;

  @Enumerated(EnumType.STRING)
  @Column(name = "state_type", nullable = false)
  private StateType stateType;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
}
