package com.codershubham.cms.cms.service.AuthModules;

import com.codershubham.cms.cms.model.AuthModel.PasswordResetTokenModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import com.codershubham.cms.cms.model.UserManagementModules.UserModel;
import com.codershubham.cms.cms.repository.AuthModules.PasswordResetTokenRepository;
import com.codershubham.cms.cms.repository.UserManagementModules.UserRepository;
import com.codershubham.cms.cms.service.StudentManagementModules.StudentService;
import com.codershubham.cms.cms.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailUtil emailUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private StudentService studentService;

    public AuthService(UserRepository userRepository, PasswordResetTokenRepository tokenRepository, EmailUtil emailUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailUtil = emailUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public void sendResetPasswordLink(String email) {
        UserModel user = userRepository.findByUsername(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        System.out.println("User Found: ");
        System.out.println("UserName: " + user.getUsername());
        System.out.println("Password: " + user.getPassword());
        System.out.println("userId: " + user.getId());
        System.out.println(user.getRole().getName().toUpperCase());

        if (user.getRole().getId() == 3) {
            StudentModel studentId = studentService.getStudentByUserId(user.getId());

            StudentModel student = studentService.findById(studentId.getId());

            PasswordResetTokenModel token = new PasswordResetTokenModel(user);
            tokenRepository.save(token);

            String resetLink = "http://localhost:8080/auth/validate-token?token=" + token.getToken();

            emailUtil.sendSimpleEmail(student.getEmail(), "Password Reset Request", "Click the link to reset your password: " + resetLink);
        }

    }

    public boolean validatePasswordResetToken(String token) {
        Optional<PasswordResetTokenModel> tokenOpt = tokenRepository.findByToken(token);

        return tokenOpt.isPresent() && !tokenOpt.get().isExpired();
    }

    public void updatePassword(String token, String newPassword) {
        PasswordResetTokenModel tokenModel = tokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if (tokenModel.isExpired()) {
            throw new RuntimeException("Token has expired");
        }

        UserModel user = tokenModel.getUser();

        System.out.println("Resetting password for user:");
        System.out.println("UserName: " + user.getUsername());
        System.out.println("UserId: " + user.getId());
        System.out.println(user.getRole().getName().toUpperCase());

        if (user.getRole().getId() == 3) { // If the user is a student
            StudentModel student = studentService.getStudentByUserId(user.getId());

            if (student == null) {
                throw new RuntimeException("Student not found for user ID: " + user.getId());
            }

            System.out.println("Student ID: " + student.getId());
            System.out.println("Student Email: " + student.getEmail());

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            tokenRepository.delete(tokenModel); // Remove the token after successful reset

            emailUtil.sendSimpleEmail(student.getEmail(), "Password Reset Successful", "Your password has been reset successfully. You can now log in with your new password.");
        } else {
            throw new RuntimeException("Password reset is only allowed for students.");
        }
    }

}
