package com.example.candread.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
            .requestMatchers("/", "/CSS/**", "/Images/**", "/SignIn", "/users/**", "/Library/**", "/SingleElement/**", "/loginerror", "/error").permitAll()
            .requestMatchers("/*/Profile/**", "/*/Main", "/review/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/Admin", "/news/add").hasRole("ADMIN")
    )
    .formLogin(formLogin -> formLogin
        .loginPage("/LogIn")
        .failureUrl("/loginerror")
        .successHandler((request, response, authentication) -> {
            String username = authentication.getName();
            String redirectUrl = "/" + username + "/Main";
            request.getSession().setAttribute("redirectUrl", redirectUrl);
            response.sendRedirect(redirectUrl);
        })
        .permitAll()
    )

    .logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")
					.permitAll()
			);

    return http.build();
}


}
