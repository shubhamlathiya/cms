package com.codershubham.cms.cms.service.UserManagementModules;

import com.codershubham.cms.cms.model.UserManagementModules.RoleModel;
import com.codershubham.cms.cms.repository.UserManagementModules.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Create a new role
    public RoleModel addRole(String roleName) {
        if (roleRepository.findByName(roleName).isPresent()) {
            throw new RuntimeException("Role already exists!");
        }

        RoleModel roleModel = new RoleModel();
        roleModel.setName(roleName.toUpperCase());
        return roleRepository.save(roleModel);
    }

    // Get all roles
    public List<RoleModel> getAllRoles() {
        return roleRepository.findAll();
    }

    // Update role name
    public RoleModel updateRole(Long id, String newName) {
        RoleModel roleModel = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found!"));

        roleModel.setName(newName.toUpperCase());
        return roleRepository.save(roleModel);
    }

    // Delete role
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
