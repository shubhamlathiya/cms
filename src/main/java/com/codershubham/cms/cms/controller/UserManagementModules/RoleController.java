package com.codershubham.cms.cms.controller.UserManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.UserManagementModules.PermissionModel;
import com.codershubham.cms.cms.model.UserManagementModules.RoleModel;
import com.codershubham.cms.cms.repository.UserManagementModules.RolePermissionRepository;
import com.codershubham.cms.cms.service.UserManagementModules.PermissionService;
import com.codershubham.cms.cms.service.UserManagementModules.RoleService;
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(PathConstant.ROLES_PATH)
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    HttpSession session;

    @Autowired
    private UserRoleUtil userRoleUtil;

    // Show all roles
    @GetMapping
    public String getAllRoles(Model model) {
        model.addAttribute("roles", roleService.getAllRoles());

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "UserManagement/roles/roles";
    }

    // Add a new role
    @PostMapping("/add")
    public String addRole(@RequestParam String roleName) {
        roleService.addRole(roleName);
        return "redirect:/" + PathConstant.ROLES_PATH;
    }

    // Update an existing role
    @PostMapping("/update")
    public String updateRole(@RequestParam Long roleId, @RequestParam String roleName) {
        roleService.updateRole(roleId, roleName);
        return "redirect:/" + PathConstant.ROLES_PATH;
    }

    // Delete a role
    @PostMapping("/delete")
    public String deleteRole(@RequestParam Long roleId) {
        roleService.deleteRole(roleId);
        return "redirect:/" + PathConstant.ROLES_PATH;
    }

    @PostMapping("/{roleId}/permissions")
    public List<PermissionModel> findPermissionsByRoleId(Long roleId) {
        // Assuming the repository returns a list of Permissions
        return rolePermissionRepository.findPermissionsByRoleId(roleId);
    }

    // Method to view and manage permissions for a role
    @GetMapping("/{roleId}/permissions")
    public String viewPermissions(@PathVariable Long roleId, Model model) {
        // Get the role by id
        RoleModel role = roleService.findById(roleId);

        // Get all permissions
        List<PermissionModel> allPermissions = permissionService.getAllPermissions();

        // Get role's current permissions (from the RolePermission relationship)
        List<PermissionModel> rolePermissions = permissionService.getPermissionsByRoleId(roleId);

        // Add attributes to the model to be used in the view (Thymeleaf or JSP)
        model.addAttribute("role", role);
        model.addAttribute("allPermissions", allPermissions);
        model.addAttribute("rolePermissions", rolePermissions);

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);
        // Return the view name (Assumes you have a template named manage-permissions.html or manage_permissions.jsp)
        return "UserManagement/roles/manage-permissions";
    }


    // Method to add permission to a role
    @PostMapping("/{roleId}/permissions/add")
    public String addPermissionToRole(@PathVariable Long roleId, @RequestParam Long permissionId) {
        roleService.addPermissionToRole(roleId, permissionId);

        return "redirect:/roles/{roleId}/permissions"; // Redirect back to the role's permissions page
    }

    // Method to remove permission from a role
    @PostMapping("/{roleId}/permissions/remove")
    public String removePermissionFromRole(@PathVariable Long roleId, @RequestParam Long permissionId) {
        roleService.removePermissionFromRole(roleId, permissionId);
        return "redirect:/roles/{roleId}/permissions"; // Redirect back to the role's permissions page
    }
}
