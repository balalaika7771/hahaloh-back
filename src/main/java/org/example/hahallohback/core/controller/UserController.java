package org.example.hahallohback.core.controller;

import base.controller.crud.jpa.CrudController;
import base.service.jpa.CrudJpaService;
import lombok.AllArgsConstructor;
import org.example.hahallohback.core.dto.UserDto;
import org.example.hahallohback.core.entity.User;
import org.example.hahallohback.core.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static base.constants.entity.EntityNames.USER;


@AllArgsConstructor
@RestController
@RequestMapping("api/" + USER)
public class UserController implements CrudController<UserDto, User, Long> {

  private final UserService userService;

  @Override
  public CrudJpaService<UserDto, User, Long> svc() {
    return userService;
  }


}
