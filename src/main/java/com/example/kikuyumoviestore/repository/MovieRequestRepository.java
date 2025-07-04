package com.example.kikuyumoviestore.repository;

import com.example.kikuyumoviestore.model.MovieRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing MovieRequest entities.
 */
@Repository
public interface MovieRequestRepository extends JpaRepository<MovieRequest, Long> {

    /**
     * Find all requests by a specific username.
     *
     * @param username the user who made the requests
     * @return list of movie requests
     */
    List<MovieRequest> findByUsername(String username);
}
