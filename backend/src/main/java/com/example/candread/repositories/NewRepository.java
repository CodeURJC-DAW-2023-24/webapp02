package com.example.candread.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.candread.model.New;

public interface NewRepository extends JpaRepository<New,Long>{
    List<New> findFirst5ByOrderByIdDesc();
}
