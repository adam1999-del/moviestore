package com.example.kikuyumoviestore.repository;

import com.example.kikuyumoviestore.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing Movie entities.
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    // Spring Data JPA will implement all CRUD methods
}
