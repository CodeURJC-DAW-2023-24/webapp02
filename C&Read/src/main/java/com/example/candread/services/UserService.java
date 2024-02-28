package com.example.candread.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.candread.model.User;
import com.example.candread.repositories.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void insertUsers() {
        if(!existsByUsernameAndPassword("admin1", "pass")){
            User userPrueba = new User("admin1", passwordEncoder.encode("pass"), "USER", "ADMIN");
            userRepository.save(userPrueba);
        }
    }
    

    public boolean existsByUsernameAndPassword(String username, String password) {
        return userRepository.findByName(username)
        .map(user -> passwordEncoder.matches(password, user.getPassword()))
        .orElse(false);
    }
}
