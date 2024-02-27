package com.example.candread.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.candread.model.User;
import com.example.candread.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void insertUsers() {

        User userPrueba = new User("admin1", passwordEncoder.encode("pass"), "ADMIN");
        userRepository.save(userPrueba);
    }
    

    
}
