package com.example.candread.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.candread.Model.User;

public interface UserRepository extends JpaRepository<User,Long> {
    
}
