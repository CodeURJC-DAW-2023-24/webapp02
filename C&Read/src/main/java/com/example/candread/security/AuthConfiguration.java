package com.example.candread.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthConfiguration {


    @Autowired
    private RepositoryUserDetailsService userDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
    // @Bean
	// public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
	// 	return authConfig.getAuthenticationManager();
	// }

    // @Bean
    // public AuthenticationManager authenticationManagerBean() throws Exception {
    //     return authentication -> {
    //         UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    //         String name = userDetails.getUsername();
    //         userDetails = userDetailService.loadUserByUsername(name);
    //         return new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(),
    //                 userDetails.getAuthorities());
    //     };
    // }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authentication -> {
            UserDetails userDetails = userDetailService.loadUserByUsername(authentication.getName());
            return new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(),
                    userDetails.getAuthorities());
        };
    }
}


