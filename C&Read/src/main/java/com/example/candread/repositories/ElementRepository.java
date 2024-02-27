package com.example.candread.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.candread.model.Element;

public interface ElementRepository extends JpaRepository<Element,Long>{
    
}
