package com.example.candread.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.candread.Model.User;
import com.example.candread.repositories.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
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
    public String addUser(@ModelAttribute User user, HttpSession session) {
        try {
            userRepository.save(user);
            session.setAttribute("user", user);
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/error";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String name, @RequestParam String password, Model model, HttpSession session) {

        Optional<User> userOptional = userRepository.findByNameAndPassword(name, password);

        if (userOptional.isPresent()) {
            // Usuario y contraseña válidos, redirige a la página principal
            User user = userOptional.get(); // Obtén el objeto User de Optional<User>
            session.setAttribute("user", user);
            return "redirect:/";
        } else {
            // Usuario o contraseña incorrectos, redirige a la página de inicio de sesión con un mensaje de error
            return "redirect:/login";
        }
    }
}
