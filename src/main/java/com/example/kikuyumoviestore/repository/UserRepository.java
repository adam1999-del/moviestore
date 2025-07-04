package com.example.kikuyumoviestore.repository;

import com.example.kikuyumoviestore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Automatically implemented by Spring Data JPA
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
