package com.example.candread.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.candread.model.Review;

public interface PagingProfileRepository extends JpaRepository<Review, Long>{
    Page<Review> findByUserLinked(Long id, Pageable pageable);  
}
