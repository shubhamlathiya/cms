package com.codershubham.cms.cms.model.UserManagementModules;

import jakarta.persistence.*;

@Entity
@Table(name = "RolePermission")
public class RolePermissionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleModel role;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private PermissionModel permission;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleModel getRole() {
        return role;
    }

    public void setRole(RoleModel role) {
        this.role = role;
    }

    public PermissionModel getPermission() {
        return permission;
    }

    public void setPermission(PermissionModel permission) {
        this.permission = permission;
    }
}
