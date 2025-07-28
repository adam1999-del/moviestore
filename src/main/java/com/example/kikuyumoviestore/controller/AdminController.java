package com.example.kikuyumoviestore.controller;

import com.example.kikuyumoviestore.model.Admin;
import com.example.kikuyumoviestore.repository.AdminRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/login")
    public String showLoginForm() {
        return "admin-login";
    }

    @PostMapping("/login") 
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              HttpSession session,
                              Model model) {

        Optional<Admin> adminOptional = adminRepository.findByUsername(username);

        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            if (admin.getPassword().equals(password)) { // Plain text comparison (not secure for production)
                session.setAttribute("admin", admin.getUsername());
                return "redirect:/admin/dashboard"; // ✅ fixed this line
            } else {
                model.addAttribute("error", "Invalid password");
                return "admin-login";
            }
        } else {
            model.addAttribute("error", "Admin not found");
            return "admin-login";
        }
    }

    // @GetMapping("/dashboard")
    // public String adminDashboard(HttpSession session) {
    //     if (session.getAttribute("admin") == null) {
    //         return "redirect:/admin/login";
    //     }
    //     return "admin-dashboard"; // ✅ Make sure this matches the HTML file name
    // }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}
