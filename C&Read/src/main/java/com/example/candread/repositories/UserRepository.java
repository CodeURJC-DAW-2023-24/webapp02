package com.example.candread.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.candread.Model.User;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByNameAndPassword(String name, String password);
    
}
