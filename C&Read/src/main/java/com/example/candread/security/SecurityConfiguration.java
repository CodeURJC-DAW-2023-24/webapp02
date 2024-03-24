package com.example.candread.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.candread.model.User;
import com.example.candread.repositories.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    public RepositoryUserDetailsService userDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/", "/CSS/**", "/Images/**", "/Scripts/**","/downloadNames/**", "/SignIn", "/users/**", "/Library/**",
                        "/SingleElement/**", "/loginerror", "/error","/EditFragment", "/api/books/**")
                .permitAll()
                .requestMatchers("/*/Profile/**", "/*/Main", "/review/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/Admin/**", "/news/add","/SingleElement/edit").hasRole("ADMIN"))
                .formLogin(formLogin -> formLogin
                        .loginPage("/LogIn")
                        .failureUrl("/loginerror")
                        .successHandler((request, response, authentication) -> {
                            String username = authentication.getName();
                            String redirectUrl = "/" + username + "/Main";
                            request.getSession().setAttribute("redirectUrl", redirectUrl);
                            response.sendRedirect(redirectUrl);
                        })
                        .permitAll())

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll());
        http.csrf(csrf->csrf.ignoringRequestMatchers("/api/**"));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authentication -> {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String name = userDetails.getUsername();
            userDetails = userDetailService.loadUserByUsername(name);
            return new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(),
                    userDetails.getAuthorities());
        };
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    public void updateSecurityContext(UserRepository userRepository, String name) {
        // Obtener el usuario actualizado de la base de datos
        Optional<User> opUser = userRepository.findByName(name);
        User user = opUser.orElseThrow(() -> new RuntimeException("User Aurora not found"));

        // Crear una instancia UserDetails basada en los nuevos detalles del usuario
        UserDetails userDetails = userDetailService.loadUserByUsername(user.getName());

        // Autenticar al usuario con los nuevos detalles utilizando el
        // AuthenticationManager
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(),
                userDetails.getAuthorities());
        Authentication authenticated = authenticationManager.authenticate(authentication);

        // Actualizar el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authenticated);
    }

}
