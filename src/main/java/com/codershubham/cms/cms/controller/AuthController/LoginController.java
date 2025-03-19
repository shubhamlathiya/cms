package com.codershubham.cms.cms.controller.AuthController;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.service.AuthModules.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    AuthService authService;

    @GetMapping(PathConstant.LOGIN_PATH)
    public String login() {
        return "AuthManagement/auth-login";
    }

    @PostMapping(PathConstant.LOGOUT_PATH)
    public String logout() {
        return "redirect:/login?logout";
    }

    @GetMapping(PathConstant.FORGOT_PASSWORD_PATH)
    public String forgotPassword() {
        return "AuthManagement/auth-resetpw";
    }

    @PostMapping(PathConstant.FORGOT_PASSWORD_PATH)
    public String forgotPasswordPost(@RequestParam("email") String email , Model model) {
        authService.sendResetPasswordLink(email);
        model.addAttribute("email", email);
        return "AuthManagement/auth-confirm";
    }

    @GetMapping(PathConstant.VALIDATE_TOKEN_PATH)
    public String validateToken(@RequestParam("token") String token, Model model) {
        boolean isValid = authService.validatePasswordResetToken(token);
        if (isValid) {
            model.addAttribute("token", token);
            return "AuthManagement/reset-password";
        }else {
            return "redirect:/" + PathConstant.FORGOT_PASSWORD_PATH;
        }
    }

    @PostMapping(PathConstant.RESET_PASSWORD_PATH)
    public String resetPassword(@RequestParam("token") String token, @RequestParam("password") String newPassword, Model model) {
        try {
            System.out.println(newPassword);
            System.out.println(token);
            authService.updatePassword(token, newPassword);
            model.addAttribute("successMessage", "Password reset successful. You can now log in.");
            return "redirect:/" + PathConstant.LOGIN_PATH;
        } catch (Exception e) {
//            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/" + PathConstant.FORGOT_PASSWORD_PATH;
        }
    }
}
