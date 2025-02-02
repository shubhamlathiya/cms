package com.codershubham.cms.cms.service;

import com.codershubham.cms.cms.model.Role;
import com.codershubham.cms.cms.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Create a new role
    public Role addRole(String roleName) {
        if (roleRepository.findByName(roleName).isPresent()) {
            throw new RuntimeException("Role already exists!");
        }

        Role role = new Role();
        role.setName(roleName.toUpperCase());
        return roleRepository.save(role);
    }

    // Get all roles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Update role name
    public Role updateRole(Long id, String newName) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found!"));

        role.setName(newName.toUpperCase());
        return roleRepository.save(role);
    }

    // Delete role
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
