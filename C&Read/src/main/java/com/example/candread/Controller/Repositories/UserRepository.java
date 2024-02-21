package com.example.candread.Controller.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.candread.Controller.Model.User;

public interface UserRepository extends JpaRepository<User,Long> {
    
}
