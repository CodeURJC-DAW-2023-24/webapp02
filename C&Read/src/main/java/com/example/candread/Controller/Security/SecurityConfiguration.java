package com.example.candread.Controller.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;



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
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
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
                .requestMatchers("/","/LogIn","/SignIn","/users/login","/users/add").permitAll()
				.requestMatchers("/javi/Main").authenticated()
				);

        http.csrf(csrf -> csrf.disable());
    
    return http.build();
}

}
