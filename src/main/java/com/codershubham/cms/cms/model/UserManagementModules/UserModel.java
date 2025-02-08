package com.codershubham.cms.cms.model.UserManagementModules;

import jakarta.persistence.*;


@Entity
@Table(name = "Users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;  // Username (e.g., email or username)

    @Column(nullable = false)
    private String password;  // Password (in practice, this should be encrypted)

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleModel role;  //  Reference to Role entity

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleModel getRole() {
        return role;
    }

    public void setRole(RoleModel role) {
        this.role = role;
    }
}
