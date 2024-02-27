package com.example.candread.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/", "/LogIn", "/SignIn", "/users/login", "/users/add", "/CSS/**", "/Images/**",
						"/*/Main")
				.permitAll()
				.requestMatchers("/Library").hasRole("USER")
				.anyRequest().authenticated()

		);

		/*http.formLogin(formLogin -> formLogin
				.loginPage("/LogIn")
				.successHandler((request, response, authentication) -> {
					String username = authentication.getName();
					String role = authentication.getAuthorities().toString();
					System.out.println("Usuario autenticado: " + username + ", Rol: " + role);
					response.sendRedirect("/" + username + "/Main");
				})
				.permitAll());*/

		http.logout((logout) -> logout
				.logoutSuccessUrl("/LogIn?logout=true")
				.permitAll());

		http.sessionManagement(sessionManagement -> sessionManagement
				.sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // Política de creación de sesiones
				.sessionFixation().migrateSession() // Estrategia para manejar la fijación de sesión
				.invalidSessionUrl("/invalidSession.html") // URL a la que redirigir en caso de sesión inválida
				.maximumSessions(1) // Número máximo de sesiones permitidas por usuario
				.expiredUrl("/sessionExpired.html"));

		http.csrf(csrf -> csrf.disable());

		return http.build();
	}

}
