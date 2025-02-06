package com.codershubham.cms.cms.service.UserManagementModules;

import com.codershubham.cms.cms.model.UserManagementModules.PermissionModel;
import com.codershubham.cms.cms.model.UserManagementModules.RoleModel;
import com.codershubham.cms.cms.model.UserManagementModules.RolePermissionModel;
import com.codershubham.cms.cms.repository.UserManagementModules.PermissionRepository;
import com.codershubham.cms.cms.repository.UserManagementModules.RolePermissionRepository;
import com.codershubham.cms.cms.repository.UserManagementModules.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    // Create a new role
    public RoleModel addRole(String roleName) {
        if (roleRepository.findByName(roleName).isPresent()) {
            throw new RuntimeException("Role already exists!");
        }

        RoleModel role = new RoleModel();
        role.setName(roleName.toUpperCase());
        return roleRepository.save(role);
    }

    // Get all roles
    public List<RoleModel> getAllRoles() {
        return roleRepository.findAll();
    }

    // Update role name
    public RoleModel updateRole(Long id, String newName) {
        RoleModel role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found!"));

        role.setName(newName.toUpperCase());
        return roleRepository.save(role);
    }

    // Delete role
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

//    public List<Permission> findPermissionsByRoleId(Long roleId) {
//        return rolePermissionRepository.findPermissionsByRoleId(roleId); // Call method on instance, not statically
//    }

    // Add permission to role
    public void addPermissionToRole(Long roleId, Long permissionId) {
        RoleModel role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        PermissionModel permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        RolePermissionModel rolePermission = new RolePermissionModel();
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);
        rolePermissionRepository.save(rolePermission);
    }

    // Remove permission from role
    public void removePermissionFromRole(Long roleId, Long permissionId) {
        RoleModel role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        PermissionModel permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        RolePermissionModel rolePermission = rolePermissionRepository.findByRoleAndPermission(role, permission)
                .orElseThrow(() -> new RuntimeException("RolePermission not found"));
        rolePermissionRepository.delete(rolePermission);
    }

    // Fetch role by ID
    public RoleModel findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }
}
