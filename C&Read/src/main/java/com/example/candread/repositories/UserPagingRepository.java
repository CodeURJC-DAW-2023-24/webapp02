package com.example.candread.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.candread.model.User;

public interface UserPagingRepository extends JpaRepository<User, Long>{
    Page<User> findAll(Pageable pageable);
}