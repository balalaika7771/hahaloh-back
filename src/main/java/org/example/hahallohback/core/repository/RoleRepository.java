package org.example.hahallohback.core.repository;

import base.repository.JpaSpecificationExecutorRepository;
import java.util.Optional;
import org.example.hahallohback.core.entity.Role;


public interface RoleRepository extends JpaSpecificationExecutorRepository<Role, Long> {

  Optional<Role> findByName(String name);

}
