package com.example.candread.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.candread.model.Review;
import com.example.candread.repositories.ReviewRepository;


@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public void repoSaveReview(Review reviewToSave){
        reviewRepository.save(reviewToSave);
    }
    public void repoDeleteReview(Review reviewToDelete){
        reviewRepository.delete(reviewToDelete);
    }
    public Optional<Review> repoFindById(Long id){
        return reviewRepository.findById(id);
    }

}
