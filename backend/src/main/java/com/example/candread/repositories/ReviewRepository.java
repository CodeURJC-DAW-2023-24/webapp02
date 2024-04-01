package com.example.candread.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.candread.model.Review;

public interface ReviewRepository extends JpaRepository<Review,Long>{
    
    Optional<Review> findById(Long id);

}
