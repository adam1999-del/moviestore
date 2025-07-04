package com.example.kikuyumoviestore.controller;

import com.example.kikuyumoviestore.model.Movie;
import com.example.kikuyumoviestore.model.MovieRequest;
import com.example.kikuyumoviestore.repository.MovieRepository;
import com.example.kikuyumoviestore.repository.MovieRequestRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired private MovieRepository movieRepository;
    @Autowired private MovieRequestRepository requestRepository;

    @GetMapping("/welcome")
    public String welcome(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        List<Movie> movies = movieRepository.findAll();
        List<MovieRequest> requests = requestRepository.findByUsername(username);

        model.addAttribute("movies", movies);
        model.addAttribute("requests", requests);
        model.addAttribute("username", username);
        return "welcome";
    }

    @PostMapping("user/request-movie")
    public String requestMovie(@RequestParam String requestedTitle, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        MovieRequest request = new MovieRequest();
        request.setUsername(username);
        request.setRequestedTitle(requestedTitle);
        request.setStatus("Pending"); // default status
        requestRepository.save(request);

        return "redirect:/welcome";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
