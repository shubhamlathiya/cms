package com.codershubham.cms.cms.controller.AuthController;

import com.codershubham.cms.cms.service.AuthModules.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private AuthService authService;



    @GetMapping("/login")
    public String login() {
        return "AuthManagement/auth-login";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }

    @GetMapping("/auth/forgot-password")
    public String forgotPassword() {
        return "AuthManagement/forgot-password";
    }

    @PostMapping("/auth/forgot-password")
    public String forgotPasswordPost(@RequestParam("email") String email) {
        authService.sendResetPasswordLink(email);
        return "AuthManagement/forgot-password";
    }

    @GetMapping("/auth/validate-token")
    public String validateToken(@RequestParam("token") String token, Model model) {
        boolean isValid = authService.validatePasswordResetToken(token);
        if (isValid) {
            model.addAttribute("token", token);
            return "AuthManagement/reset-password";
        }else {
            return "/auth/forgot-password";
        }
    }

    @PostMapping("/auth/reset-password")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("password") String newPassword, Model model) {
        try {
            System.out.println(newPassword);
            System.out.println(token);
            authService.updatePassword(token, newPassword);
            model.addAttribute("successMessage", "Password reset successful. You can now log in.");
            return "redirect:/auth/login";
        } catch (Exception e) {
//            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/auth/forgot-password";
        }
    }
}
