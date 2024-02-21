package com.example.candread.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.example.candread.Model.User;
import com.example.candread.repositories.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        return userOptional.map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ModelAndView addUser(@ModelAttribute User user) {
        try {
            userRepository.save(user);
            ModelAndView modelAndView = new ModelAndView("W-Main");
            // Puedes agregar objetos al modelo si es necesario
            modelAndView.addObject("message", "Usuario agregado exitosamente");
            return modelAndView;
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("error-page");
            // Puedes agregar objetos al modelo si es necesario
            modelAndView.addObject("error", "Error al agregar usuario");
            return modelAndView;
        }
    }
}