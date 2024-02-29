package com.example.candread.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.candread.model.Review;

public interface ReviewRepository extends JpaRepository<Review,Long>{

    Page<Review> findByUserLinked(Long id, Pageable pageable);
    
}
