package org.example.hahallohback.core.service;

import base.repository.JpaSpecificationExecutorRepository;
import base.service.jpa.CrudJpaService;
import base.transformer.Transformer;
import lombok.AllArgsConstructor;
import org.example.hahallohback.core.dto.RoleDto;
import org.example.hahallohback.core.entity.Role;
import org.example.hahallohback.core.repository.RoleRepository;
import org.example.hahallohback.core.transformer.RoleTransformer;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class RoleService implements CrudJpaService<RoleDto, Role, Long> {

  private final RoleRepository roleRepository;

  private final RoleTransformer roleTransformer;

  @Override
  public JpaSpecificationExecutorRepository<Role, Long> repo() {
    return roleRepository;
  }

  @Override
  public Transformer<RoleDto, Role> t() {
    return roleTransformer;
  }
}
