package com.codershubham.cms.cms.service.UserManagementModules;

import com.codershubham.cms.cms.model.UserManagementModules.PermissionModel;
import com.codershubham.cms.cms.repository.UserManagementModules.PermissionRepository;
import com.codershubham.cms.cms.repository.UserManagementModules.RolePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// PermissionService.java
@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    // Get all permissions
    public List<PermissionModel> getAllPermissions() {
        return permissionRepository.findAll();
    }

    // Method to get permissions by roleId
//    public List<Permission> getPermissionsByRoleId(Long roleId) {
//        // Querying the repository, expecting Tuple results
//        List<Tuple> results = rolePermissionRepository.findPermissionsByRoleId(roleId);
//
//        // Mapping the results from Tuple to Permission entities
//        return results.stream()
//                .map(tuple -> {
//                    // Assuming you have a column named "permission" in the Tuple
//                    Permission permission = tuple.get(0, Permission.class); // Accessing the Permission entity from Tuple
//                    return permission;
//                })
//                .collect(Collectors.toList());
//    }

    public List<PermissionModel> getPermissionsByRoleId(Long roleId) {
        return rolePermissionRepository.findPermissionsByRoleId(roleId);
    }

//    public List<Permission> getPermissionsByRole(Long roleId) {
//        // Assuming a method in the repository that fetches permissions by roleId
//        return permissionRepository.findByRoleId(roleId);
//    }
}
