package com.codershubham.cms.cms.repository.UserManagementModules;

import com.codershubham.cms.cms.model.UserManagementModules.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    Optional<RoleModel> findByName(String name);
}
