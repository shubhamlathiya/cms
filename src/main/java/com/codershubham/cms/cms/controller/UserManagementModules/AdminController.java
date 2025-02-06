package com.codershubham.cms.cms.controller.UserManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(PathConstant.ADMIN_PATH)
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "index";
    }

    @GetMapping("/users")
    public String users() {
        return "users";
    }


}
