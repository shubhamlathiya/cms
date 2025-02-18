package com.codershubham.cms.cms.controller.AuthController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "AuthManagement/auth-login";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }

//
//    @GetMapping("/redirect")
//    public String redirectBasedOnRole(Authentication authentication) {
//        // Get the logged-in user's role
//        String role = authentication.getAuthorities().stream()
//                .findFirst()
//                .map(GrantedAuthority::getAuthority)
//                .orElse("");
//
//        // Redirect based on role
//        if (role.equals("ROLE_ADMIN")) {
//            return "redirect:/admin/dashboard";
//        } else if (role.equals("ROLE_FACULTY")) {
//            return "redirect:/faculty/dashboard";
//        } else if (role.equals("ROLE_STUDENT")) {
//            return "redirect:/student/dashboard";
//        }
//
//        // Default fallback
//        return "redirect:/login?error";
//    }
}
