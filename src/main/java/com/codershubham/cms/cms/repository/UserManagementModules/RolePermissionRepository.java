package com.codershubham.cms.cms.repository.UserManagementModules;

import com.codershubham.cms.cms.model.UserManagementModules.PermissionModel;
import com.codershubham.cms.cms.model.UserManagementModules.RoleModel;
import com.codershubham.cms.cms.model.UserManagementModules.RolePermissionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionModel, Long> {

    // Query to fetch a list of Permissions associated with a specific Role
    @Query("SELECT rp.permission FROM RolePermissionModel rp WHERE rp.role.id = :roleId")
    List<PermissionModel> findPermissionsByRoleId(@Param("roleId") Long roleId);

    // Additional method to check if a RolePermission exists between a given Role and Permission
    Optional<RolePermissionModel> findByRoleAndPermission(RoleModel role, PermissionModel permission);
}
