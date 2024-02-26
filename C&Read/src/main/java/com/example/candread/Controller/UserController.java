package com.example.candread.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.candread.model.User;
import com.example.candread.repositories.UserRepository;
import com.example.candread.Security.RepositoryUserDetailsService;

import org.springframework.security.core.userdetails.UserDetails;
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
    public RepositoryUserDetailsService userDetailService;

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
            //se crea un usuario y se le asigna uno o varios roles
            User userPrueba = new User(user.getName(), user.getPassword(), "USER");
            userRepository.save(userPrueba);

            // userRepository.save(encryptedUser);

            return "redirect:/LogIn";
        } catch (Exception e) {
            return "redirect:/SignIn";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String name, @RequestParam String password, Model model, HttpSession session) {

        Optional<User> userOptional = userRepository.findByNameAndPassword(name, password);

        if (userOptional.isPresent()) {
            // Usuario y contraseña válidos, redirige a la página principal
            User user = (User) userOptional.get(); // Obtenemos el objeto User de Optional<User>
            session.setAttribute("user", user);
            UserDetails a = userDetailService.loadUserByUsername(name);
            return "redirect:/" + user.getName() + "/Main";

        } else {
            // Usuario o contraseña incorrectos, redirige a la página de inicio de sesión con un mensaje de error
            return "redirect:/LogIn";
        }
    }
    
}
