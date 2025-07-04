package com.example.kikuyumoviestore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage() {
        return "home"; // Renders home.html
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // Renders signup.html
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Renders login.html
    }
}
