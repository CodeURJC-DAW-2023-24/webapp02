package com.example.candread.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.candread.model.Element;
import com.example.candread.model.User;



public interface PagingRepository extends JpaRepository<Element, Long>{

    Page<Element> findByAuthor(String author, Pageable page);
    Page<Element> findBySeason(String season, Pageable page);
    Page<Element> findByType(String type, Pageable page);
    Page<Element> findByGenres(String type, Pageable page);
    Page<Element> findByCountry(String country, Pageable pageable);
    Page<Element> findByState(String state, Pageable pageable);
    Page<Element> findByUsersIdAndType(Long id, String type, Pageable pageable);
    //Page<Element> findByYear(String year, Pageable pageable);
    Page<Element> findByTypeAndGenres(String string, String genre, Pageable pageable);
    
} 
