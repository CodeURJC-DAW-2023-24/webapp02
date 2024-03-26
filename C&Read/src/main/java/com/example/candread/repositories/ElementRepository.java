package com.example.candread.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.candread.model.Element;


public interface ElementRepository extends JpaRepository<Element,Long>{

    @Query("SELECT e FROM Element e WHERE e.type = :type ORDER BY (SELECT AVG(r.rating) FROM e.reviews r) DESC")
    Page<Element> findTopElementsByRating(String type, Pageable pageable);

    List<Element> findTop5ByOrderByIdDesc();

    List<Element> findTop4ByOrderByIdDesc();

    List<Element> findByName(String name);

    Optional<Element> findById(long id);

    Optional<Element> findByIdAndType(long id, String type);

    void deleteByName(String name);
}
