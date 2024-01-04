package cz.qjetta.birthdays.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import cz.qjetta.birthdays.service.UserInfoService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private static final String[] WHITELIST = {
			// swagger
			"/api/v1/auth/**", "/v3/api-docs/**", "/v3/api-docs.yaml",
			"/swagger-ui/**", "/swagger-ui.html", "/configuration/security",
			"/webjars/**", "/swagger.json", "/swagger-resources/**",

			//
			"/welcome", "/register", "/login" };

	@Autowired
	private JwtAuthFilter authFilter;

	// User Creation
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserInfoService();
	}

	// Configuring HttpSecurity
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)
			throws Exception {

		http.csrf(AbstractHttpConfigurer::disable);

		http.authorizeHttpRequests(request -> {
			request.requestMatchers(WHITELIST).permitAll();
		});

		http.authorizeHttpRequests(request -> {
			request.requestMatchers("/**").authenticated();
		});
		http.sessionManagement(sess -> {
			sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		});
		http.authenticationProvider(authenticationProvider()).addFilterBefore(
				authFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();

	}

	// Password Encoding
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
