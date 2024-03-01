package com.example.candread.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.candread.model.Review;
import com.example.candread.model.User;

public interface ReviewRepository extends JpaRepository<Review,Long>{

    User findByUserLinked(User i);
    
}
