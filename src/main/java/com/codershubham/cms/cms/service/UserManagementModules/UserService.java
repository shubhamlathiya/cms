package com.codershubham.cms.cms.service.UserManagementModules;

import com.codershubham.cms.cms.model.UserManagementModules.UserModel;
import com.codershubham.cms.cms.repository.UserManagementModules.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // For secure password storage

    // Create a new user
    public UserModel createUser(UserModel user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
        return userRepository.save(user);
    }
//
//    // Get all users
//    public List<UserModel> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    // Get user by ID
    public UserModel getUserById(Long userId) {
        Optional<UserModel> user = userRepository.findById(userId);
        return user.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    // Update an existing user
//    public UserModel updateUser(Long id, UserModel userDetails) {
//        Optional<UserModel> optionalUser = userRepository.findById(id);
//        if (optionalUser.isPresent()) {
//            UserModel user = optionalUser.get();
//            user.setName(userDetails.getName());
//            user.setEmail(userDetails.getEmail());
//            user.setRole(userDetails.getRole());
//            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
//                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
//            }
//            return userRepository.save(user);
//        } else {
//            throw new RuntimeException("User not found with ID: " + id);
//        }
//    }

    // Delete user by ID
//    public void deleteUser(Long id) {
//        userRepository.deleteById(id);
//    }

    // Get user by email (for authentication)
//    public Optional<UserModel> getUserByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }
}
