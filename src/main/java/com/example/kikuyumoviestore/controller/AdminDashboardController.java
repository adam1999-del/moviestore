package com.example.kikuyumoviestore.controller;

import com.example.kikuyumoviestore.model.Admin;
import com.example.kikuyumoviestore.model.Movie;
import com.example.kikuyumoviestore.model.MovieRequest;
import com.example.kikuyumoviestore.model.User;
import com.example.kikuyumoviestore.repository.AdminRepository;
import com.example.kikuyumoviestore.repository.MovieRepository;
import com.example.kikuyumoviestore.repository.MovieRequestRepository;
import com.example.kikuyumoviestore.repository.UserRepository;
import com.example.kikuyumoviestore.service.EmailService;
import com.example.kikuyumoviestore.service.MovieRequestService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieRequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MovieRequestService movieRequestService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session,
                             @RequestParam(value = "success", required = false) String success,
                             @RequestParam(value = "error", required = false) String error) {

        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("totalMovies", movieRepository.count());
        model.addAttribute("totalRequests", requestRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("movies", movieRepository.findAll());
        model.addAttribute("requests", requestRepository.findAll());
        model.addAttribute("monthlyMovieData", getMoviesPerMonth());
        model.addAttribute("monthlyRequestData", getRequestsPerMonth());

        if (success != null) {
            model.addAttribute("message", success);
        }
        if (error != null) {
            model.addAttribute("error", error);
        }

        return "admin-dashboard";
    }

    @PostMapping("/add-movie")
    public String addMovie(@RequestParam String title,
                           @RequestParam("cover") MultipartFile cover,
                           @RequestParam(value ="trailer", required = false) MultipartFile trailer,
                           HttpSession session) {

        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        try {
            Movie movie = new Movie();
            movie.setTitle(title);
            movie.setCoverImage(cover.getBytes());
            movie.setCoverImageType(cover.getContentType());

            if (trailer != null && !trailer.isEmpty()) {
                movie.setTrailerData(trailer.getBytes());
                movie.setTrailerType(trailer.getContentType());
            }

            movieRepository.save(movie);

            List<User> users = userRepository.findAll();
            for (User user : users) {
                try {
                    emailService.sendNewMovieEmail(user.getEmail(), movie);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

            return "redirect:/admin/dashboard?success=Movie added successfully";

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/dashboard?error=Failed to upload movie";
        }
    }

    @GetMapping("/movie/image/{id}")
    public ResponseEntity<byte[]> getMovieImage(@PathVariable Long id) {
        Optional<Movie> movieOpt = movieRepository.findById(id);
        if (movieOpt.isPresent() && movieOpt.get().getCoverImage() != null) {
            byte[] image = movieOpt.get().getCoverImage();

            String contentType = movieOpt.get().getCoverImageType();
            if (contentType == null) {
                try {
                    contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(image));
                } catch (IOException e) {
                    contentType = "image/jpeg";
                }
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/movie/trailer/{id}")
    public ResponseEntity<byte[]> getMovieTrailer(@PathVariable Long id) {
        Optional<Movie> movieOpt = movieRepository.findById(id);
        if (movieOpt.isPresent() && movieOpt.get().getTrailerData() != null) {
            Movie movie = movieOpt.get();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(movie.getTrailerType()));
            return new ResponseEntity<>(movie.getTrailerData(), headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

 @PostMapping("/update-request-status")
public String updateRequestStatus(@RequestParam Long requestId,
                                  @RequestParam String status,
                                  HttpSession session) {
    if (session.getAttribute("admin") == null) {
        return "redirect:/admin/login";
    }

    requestRepository.findById(requestId).ifPresent(request -> {
        request.setStatus(status);
        requestRepository.save(request);

        // Fetch user by username
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String toEmail = user.getEmail();
            String requestedTitle = request.getRequestedTitle();

            try {
                emailService.sendRequestStatusUpdateEmail(toEmail, requestedTitle, status);
            } catch (MessagingException e) {
                e.printStackTrace(); // Optionally log or handle better
            }
        }
    });

    return "redirect:/admin/dashboard";
}


    @PostMapping("/edit-movie")
    public String editMovie(@RequestParam Long movieId,
                             @RequestParam String newTitle,
                             HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        movieRepository.findById(movieId).ifPresent(movie -> {
            movie.setTitle(newTitle);
            movieRepository.save(movie);
        });

        return "redirect:/admin/dashboard";
    }

    @PostMapping("/delete-movie")
    public String deleteMovie(@RequestParam Long movieId,
                               HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        movieRepository.deleteById(movieId);

        return "redirect:/admin/dashboard";
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {

        String adminUsername = (String) session.getAttribute("admin");
        if (adminUsername == null) {
            return "redirect:/admin/login";
        }

        Optional<Admin> adminOpt = adminRepository.findByUsername(adminUsername);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();

            if (!admin.getPassword().equals(currentPassword)) {
                model.addAttribute("passwordError", "Current password is incorrect.");
                return "admin-dashboard";
            } else if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("passwordError", "New passwords do not match.");
                return "admin-dashboard";
            } else {
                admin.setPassword(newPassword);
                adminRepository.save(admin);
                model.addAttribute("passwordSuccess", "Password updated successfully.");
                return "admin-dashboard";
            }
        }

        return "redirect:/admin/login";
    }

    private Map<String, Long> getMoviesPerMonth() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .collect(Collectors.groupingBy(
                        m -> Month.of(m.getId().intValue() % 12 + 1).name(),
                        Collectors.counting()));
    }

    private Map<String, Long> getRequestsPerMonth() {
        List<MovieRequest> requests = requestRepository.findAll();
        return requests.stream()
                .collect(Collectors.groupingBy(
                        r -> Month.of(r.getId().intValue() % 12 + 1).name(),
                        Collectors.counting()));
    }

    @PostMapping("/delete-request")
    public String deleteMovieRequest(@RequestParam("requestId") Long requestId, RedirectAttributes redirectAttributes) {
        try {
            movieRequestService.deleteById(requestId);
            redirectAttributes.addFlashAttribute("successMessage", "Request cleared successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to clear the request.");
        }
        return "redirect:/admin/dashboard";
    }
}
