package com.codershubham.cms.cms.repository.UserManagementModules;

import com.codershubham.cms.cms.model.UserManagementModules.PermissionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionModel, Long> {
//    Optional<PermissionModel> findByPermissionName(String permissionName);

}
