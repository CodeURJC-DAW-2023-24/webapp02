package com.example.candread.Controller.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.candread.Controller.Model.User;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByNameAndPassword1(String name, String password1);
    Optional<User> findByName(String name);
    
}
