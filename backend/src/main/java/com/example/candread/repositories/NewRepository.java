package com.example.candread.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.candread.model.New;

public interface NewRepository extends JpaRepository<New,Long>{
    
}
