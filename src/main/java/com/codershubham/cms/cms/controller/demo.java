package com.codershubham.cms.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class demo {

    @GetMapping("/")
    public String home() {
        return "home"; // Renders home.html
    }

}
