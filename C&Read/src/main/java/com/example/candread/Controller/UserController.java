package com.example.candread.Controller;

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

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

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

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        return userOptional.map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

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

    @PostMapping("/login")
    public String login(@RequestParam String name, @RequestParam String password, Model model, HttpSession session) {

        UserDetails userDetails = userDetailService.loadUserByUsername(name);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                password);
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            // Establece el contexto de seguridad en la sesión
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Guarda el contexto de seguridad en la sesión
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());
            session.setAttribute("user", userDetails);
            return "redirect:/" + name + "/Main";
        } catch (AuthenticationException e) {
            return "redirect:/LogIn";
        }
    }

}
