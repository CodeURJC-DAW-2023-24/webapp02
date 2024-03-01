package com.example.candread.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.candread.model.Element;



public interface PagingRepository extends JpaRepository<Element, Long>{

    Page<Element> findByAuthor(String author, Pageable page);
    Page<Element> findBySeason(String season, Pageable page);
    Page<Element> findByType(String type, Pageable page);
    Page<Element> findByGenres(String type, Pageable page);
    Page<Element> findByCountry(String country, Pageable pageable);
    Page<Element> findByState(String state, Pageable pageable);
    //Page<Element> findByYear(String year, Pageable pageable);
    
} 
