package com.codershubham.cms.cms.controller.UserManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.service.UserManagementModules.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(PathConstant.ROLES_PATH)
public class RoleController {

    @Autowired
    private RoleService roleService;

    // Show all roles
    @GetMapping
    public String getAllRoles(Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "UserManagement/roles/roles";
    }

    // Add a new role
    @PostMapping("/add")
    public String addRole(@RequestParam String roleName) {
        roleService.addRole(roleName);
        return "redirect:/roles";
    }

    // Update an existing role
    @PostMapping("/update")
    public String updateRole(@RequestParam Long roleId, @RequestParam String roleName) {
        roleService.updateRole(roleId, roleName);
        return "redirect:/roles";
    }

    // Delete a role
    @PostMapping("/delete")
    public String deleteRole(@RequestParam Long roleId) {
        roleService.deleteRole(roleId);
        return "redirect:/roles";
    }
}
