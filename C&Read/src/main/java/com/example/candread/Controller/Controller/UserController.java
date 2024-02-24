package com.example.candread.Controller.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.candread.Controller.Model.User;
import com.example.candread.Controller.Repositories.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.http.HttpSession;

@Configuration
@Controller
@RequestMapping("/users")
public class UserController {


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        return userOptional.map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        try {
            //se guarda usuario nuevo en la base de datos
            // User encryptedUser = new User();
            // encryptedUser.setName("user2");
            // encryptedUser.setPassword1(passwordEncoder.encode("pass2"));
            userRepository.save(user);
            // userRepository.save(encryptedUser);

            return "redirect:/LogIn";
        } catch (Exception e) {
            return "redirect:/SignIn";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String name, @RequestParam String password1, Model model, HttpSession session) {
        Optional<User> userOptional = userRepository.findByNameAndPassword1(name, password1);

        if (userOptional.isPresent()) {
            // Usuario y contraseña válidos, redirige a la página principal
            User user = (User) userOptional.get(); // Obtén el objeto User de Optional<User>
            session.setAttribute("user", user);
            return "redirect:/" + user.getName() + "/Main";

        } else {
            // Usuario o contraseña incorrectos, redirige a la página de inicio de sesión con un mensaje de error
            return "redirect:/LogIn";
        }
    }
    
}
