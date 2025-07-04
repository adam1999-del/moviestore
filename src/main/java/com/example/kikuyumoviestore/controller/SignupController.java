package com.example.kikuyumoviestore.controller;

import com.example.kikuyumoviestore.model.User;
import com.example.kikuyumoviestore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignupController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public String handleSignup(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            redirectAttributes.addFlashAttribute("signupError", "Username already exists.");
            return "redirect:/signup";
        }

        // Save the user to the database
        userRepository.save(user);

        // Show success message and redirect to login
        redirectAttributes.addFlashAttribute("signupSuccess", "Account created successfully!");
        return "redirect:/login";
    }
}
