package com.codershubham.cms.cms.controller.UserManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.util.UserRoleUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(PathConstant.ADMIN_PATH)
public class AdminController {

    @Autowired
    private UserRoleUtil userRoleUtil;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {

        String userRole = userRoleUtil.getUserRole(session);
        model.addAttribute("userRole", userRole);

        return "UserManagement/admin/dashboard";
    }

    @GetMapping("/users")
    public String users() {
        return "index";
    }


}
