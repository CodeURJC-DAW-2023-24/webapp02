package com.example.candread.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.candread.model.Element;

public interface ElementRepository extends JpaRepository<Element,Long>{
    
    List<Element> findTop5ByOrderByIdDesc();

    List<Element> findTop4ByOrderByIdDesc();
}
