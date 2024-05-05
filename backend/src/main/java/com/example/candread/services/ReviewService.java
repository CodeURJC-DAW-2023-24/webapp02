package com.example.candread.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.candread.model.Review;
import com.example.candread.repositories.ReviewRepository;


@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review repoSaveReview(Review reviewToSave){
        Review review = reviewRepository.save(reviewToSave);
        return review;
    }
    public void repoDeleteReview(Review reviewToDelete){
        reviewRepository.delete(reviewToDelete);
    }
    public Optional<Review> repoFindById(Long id){
        return reviewRepository.findById(id);
    }

    public List<Review> repoFindAll(){
        return reviewRepository.findAll();
    }

}
