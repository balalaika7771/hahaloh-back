package org.example.hahallohback.core.controller;

import base.controller.crud.jpa.CrudController;
import base.service.jpa.CrudJpaService;
import lombok.AllArgsConstructor;
import org.example.hahallohback.core.dto.RoleDto;
import org.example.hahallohback.core.entity.Role;
import org.example.hahallohback.core.service.RoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static base.constants.entity.EntityNames.ROLE;


@AllArgsConstructor
@RestController
@RequestMapping(ROLE)
public class RoleController implements CrudController<RoleDto, Role, Long> {

  private final RoleService roleService;

  @Override
  public CrudJpaService<RoleDto, Role, Long> svc() {
    return roleService;
  }
}
