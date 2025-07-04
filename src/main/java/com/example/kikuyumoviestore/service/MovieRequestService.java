package com.example.kikuyumoviestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.kikuyumoviestore.repository.MovieRequestRepository;

@Service
public class MovieRequestService {
@Autowired
    private final MovieRequestRepository movieRequestRepository;

    
    
    public MovieRequestService(MovieRequestRepository movieRequestRepository) {
        this.movieRequestRepository = movieRequestRepository;
    }

    public void deleteById(Long requestId) {
        movieRequestRepository.deleteById(requestId);
    }
}
