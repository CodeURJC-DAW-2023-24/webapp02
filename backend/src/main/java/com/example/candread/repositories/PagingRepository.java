package com.example.candread.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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
    Page<Element> findByUsersIdAndType(Long id, String type, Pageable pageable);
    Page<Element> findByTypeAndGenres(String string, String genre, Pageable pageable);
    Page<Element> findByUsersFavouritedIdAndType(Long id, String type, Pageable pageable);
    Page<Element> findByTypeAndSeason(String string, String season, Pageable pageable);
    Page<Element> findByTypeAndCountry(String string, String country, Pageable pageable);
    Page<Element> findByTypeAndState(String string, String state, Pageable pageable);
    
    @Query("SELECT e FROM Element e WHERE e.type = :type AND e IN :recomendation")
    Page<Element> findByTypeAndRecommendations(String type, List<Element> recomendation, Pageable pageable);
    
} 
