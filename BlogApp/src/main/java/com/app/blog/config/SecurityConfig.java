package com.app.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.app.blog.security.CustomeUserDetailService;
import com.app.blog.security.JWTAuthenticationEntryPoint;
import com.app.blog.security.JWTAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	@Autowired
	private CustomeUserDetailService customeUserDetailService;
	@Autowired
	private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Autowired
	private JWTAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
	CustomeUserDetailService userdetails;
	
	public static final String publicURL[] = {
		    "/api/v1/auth/**",
		    "/api/singlePosts/2",
		    "/error",
		    "/v3/api-docs/**",    // OpenAPI specification
		    "/v2/api-docs/**", 
		    "/swagger-ui/**",      // Swagger UI resources
		    "/swagger-resources/**", // Swagger resources
		    "/configuration/ui", 
		    "/configuration/security",
		    "/webjars/**"          // Required for Swagger UI assets
		};


	 @Bean
	    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .csrf(csrf -> csrf.disable()) // Disable CSRF protection
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers(publicURL).permitAll() // Allow public URLs
	                .requestMatchers(HttpMethod.GET).permitAll() // Allow all GET requests
	                .anyRequest().authenticated() // Require authentication for all other requests
	            )
	            .exceptionHandling(ex -> ex
	                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Handle unauthorized access
	            )
	            .sessionManagement(session -> session
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use stateless sessions (JWT-based authentication)
	            )
	            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

	        return http.build(); // Build and return SecurityFilterChain
	    }

	protected void customeAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		// Configuring authentication manager to use a custom UserDetailsService
		// and a BCrypt password encoder for securing passwords
		auth.userDetailsService(this.userdetails).passwordEncoder(passwordEncoder());
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		// Using BCryptPasswordEncoder to securely hash passwords
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
