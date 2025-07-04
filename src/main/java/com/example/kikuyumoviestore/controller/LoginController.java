package com.example.kikuyumoviestore.controller;

import com.example.kikuyumoviestore.model.User;
import com.example.kikuyumoviestore.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              Model model,
                              HttpSession session) {

        System.out.println("🔍 Login attempt for: " + username);

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            System.out.println("❌ Username not found");
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        User user = userOptional.get();
        System.out.println("✅ Found user: " + user.getUsername());
        System.out.println("DB password: " + user.getPassword());

        if (!user.getPassword().equals(password)) {
            System.out.println("❌ Password mismatch");
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        System.out.println("✅ Login success. Redirecting...");
        session.setAttribute("username", user.getUsername());
        return "redirect:/welcome";
    }

}
