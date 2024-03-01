package com.example.candread.services;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import com.example.candread.model.Element;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.candread.model.User;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.repositories.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ElementRepository elementRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ElementService elementService;

    @PostConstruct
    public void insertUsers() throws SerialException, IOException, SQLException {

        //BASE USERS IN THE SYSTEM: 1 ADMIN 1 USER 1 ADMIN-USER
        if(!existsByUsernameAndPassword("admin1", "pass")){
            User userPrueba = new User("admin1", passwordEncoder.encode("pass"), "USER", "ADMIN");
            userRepository.save(userPrueba);
        }
        if(!existsByUsernameAndPassword("admin3", "123")){
            User userPrueba = new User("admin3", passwordEncoder.encode("123"),  "ADMIN");
            userRepository.save(userPrueba);
        }

        if(!existsByUsernameAndPassword("Antonio27", "pass")){
            User userPrueba2 = new User("Antonio27", passwordEncoder.encode("pass"), "USER");
            userRepository.save(userPrueba2);

        }

        //ElementService created to execute after users creation
        elementService.insertElement();


    }
    

    public boolean existsByUsernameAndPassword(String username, String password) {
        return userRepository.findByName(username)
        .map(user -> passwordEncoder.matches(password, user.getPassword()))
        .orElse(false);
    }
}
