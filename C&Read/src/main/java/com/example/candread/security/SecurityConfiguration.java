package com.example.candread.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.candread.model.User;
import com.example.candread.repositories.UserRepository;
import com.example.candread.services.UserService;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public RepositoryUserDetailsService userDetailService;

    @Autowired
    private Filter jwtRequestFilter;

    @Autowired
    private AuthenticationEntryPoint unauthorizedHandlerJwt;

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
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/", "/CSS/**", "/Images/**", "/Scripts/**","/downloadNames/**", "/SignIn", "/users/**", "/Library/**",
                        "/SingleElement/**", "/loginerror", "/error","/EditFragment")
                .permitAll()
                .requestMatchers("/*/Profile/**", "/*/Main", "/review/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/Admin/**", "/news/add","/SingleElement/edit").hasRole("ADMIN")
                .requestMatchers("/swagger-ui/**","/v3/**").permitAll())
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
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {

        http
			.securityMatcher("/api/**")
			.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));


            http
			.authorizeHttpRequests(authorize -> authorize
                    // PRIVATE ENDPOINTS
                    .requestMatchers("/api/users/**").hasRole("ADMIN")
					// PUBLIC ENDPOINTS
					.anyRequest().permitAll()
			);
		
        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();

            
    }



    

    public void updateSecurityContext(UserService userService, String name) {
        // Obtener el usuario actualizado de la base de datos
        //Optional<User> opUser = userRepository.findByName(name);
        Optional<User> opUser = userService.repoFindByNameOpt(name);
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