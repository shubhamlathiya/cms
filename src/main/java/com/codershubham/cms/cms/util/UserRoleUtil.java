package com.codershubham.cms.cms.util;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserRoleUtil {

    public String getUserRole(HttpSession session) {
        // Check if userRole exists in session
        String userRole = (String) session.getAttribute("userRole");

        // If userRole is not in session, fetch from authentication context
        if (userRole == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getAuthorities() != null && !auth.getAuthorities().isEmpty()) {
                userRole = auth.getAuthorities().iterator().next().getAuthority(); // Get first role
                session.setAttribute("userRole", userRole); // Save to session
            }
        }
        return userRole;
    }
}
