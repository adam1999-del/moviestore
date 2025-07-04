package com.example.kikuyumoviestore.service;

import com.example.kikuyumoviestore.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendNewMovieEmail(String to, Movie movie) throws MessagingException {
        String subject = "🎬 New Movie Added: " + movie.getTitle();
        String content = buildNewMovieEmailContent(movie);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true); // true = is HTML

        mailSender.send(message);
    }

    public String buildNewMovieEmailContent(Movie movie) {
        String baseUrl = "http://localhost:8080"; // Replace with actual domain if deployed

        String trailerUrl = baseUrl + "/movie/trailer/" + movie.getId();
        String coverImageUrl = baseUrl + "/admin/movie/image/" + movie.getId();

        return "<html>" +
                "<body style='font-family:Arial,sans-serif; color:#333;'>"
                + "<h2 style='color:#1e40af;'>🎬 New Movie Alert!</h2>"

                + "<p><strong>🎉 A fantastic new movie titled "
                + "<span style='color:#2a9d8f;'>" + movie.getTitle() + "</span> has just been added to Kikuyu Movie Store!</strong></p>"

                + "<p>Don't miss out—go check it out and enjoy. Make your day joyful with <strong>Kikuyu Movie Store</strong>!</p>"

                + "<hr style='border:none;border-top:1px solid #ccc;margin:20px 0;'/>"

                + "<p>✅ <strong>Movie Title:</strong> " + movie.getTitle() + "</p>"

                + "<p>🖼️ <strong>Movie Cover Image:</strong><br/>"
                + "<img src='" + coverImageUrl + "' alt='Movie Cover' style='width:320px; border-radius:10px; box-shadow:0 4px 6px rgba(0,0,0,0.1);'/></p>"

                + "<p>🔗 <strong>Watch Trailer:</strong> <a href='" + trailerUrl + "' style='color:#ef4444; text-decoration:none;'>Click here to watch the trailer</a></p>"

                + "<br/><p style='font-size:0.9em;color:#666;'>Thank you for being a valued user of Kikuyu Movie Store!</p>"
                + "</body></html>";
    }

    public void sendRequestStatusUpdateEmail(String toEmail, String movieTitle, String status) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(toEmail);
    helper.setSubject("Your Movie Request Status Has Been Updated");

    String content = "<p>Dear User,</p>"
                   + "<p>Your request for the movie <strong>" + movieTitle + "</strong> has been updated.</p>"
                   + "<p>New Status: <strong>" + status + "</strong></p>"
                   + "<p>Thank you for using Kikuyu Movie Store!</p>";

    helper.setText(content, true);
    mailSender.send(message);
}

}
