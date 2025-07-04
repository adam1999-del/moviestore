package com.example.kikuyumoviestore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "movie_requests")
public class MovieRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Username of the user who made the request
    @Column(nullable = false)
    private String username;

    // Movie title being requested
    @Column(nullable = false)
    private String requestedTitle;

    // Status: e.g. "Available", "Not Found", "Pending"
    @Column(nullable = false)
    private String status;

    // ======= Constructors =======

    public MovieRequest() {
    }

    public MovieRequest(String username, String requestedTitle, String status) {
        this.username = username;
        this.requestedTitle = requestedTitle;
        this.status = status;
    }

    // ======= Getters and Setters =======

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRequestedTitle() {
        return requestedTitle;
    }

    public void setRequestedTitle(String requestedTitle) {
        this.requestedTitle = requestedTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
