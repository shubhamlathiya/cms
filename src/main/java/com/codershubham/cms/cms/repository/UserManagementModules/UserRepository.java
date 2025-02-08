package com.codershubham.cms.cms.repository.UserManagementModules;

import com.codershubham.cms.cms.model.UserManagementModules.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserModel, Long> {
//    Optional<UserModel> findByUsername(String username);
    // Check if a username already exists
    boolean existsByUsername(String username);

}
