package com.example.candread.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.candread.model.News;

public interface NewRepository extends JpaRepository<News,Long>{
    
}
