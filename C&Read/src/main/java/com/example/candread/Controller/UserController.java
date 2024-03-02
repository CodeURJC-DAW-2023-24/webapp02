package com.example.candread.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.candread.model.User;
import com.example.candread.repositories.UserRepository;
import com.example.candread.Security.RepositoryUserDetailsService;


import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public RepositoryUserDetailsService userDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        try {
            // se crea un usuario y se le asigna uno o varios roles
            User userPrueba = new User(user.getName(), passwordEncoder.encode(user.getPassword()), "USER");
            userRepository.save(userPrueba);

            // userRepository.save(encryptedUser);

            return "redirect:/LogIn";
        } catch (Exception e) {
            return "redirect:/SignIn";
        }
    }

}
