package com.app.blog.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JWTTokenHelper jwtTokenHelper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// get token
		String requestToken = request.getHeader("Authorization");
		log.info("request token:{}", requestToken);
		// Bearer 2323errrs
		String userName = "";
		String token = "";

		 String requestURI = request.getRequestURI();

		    // Skip authentication for permitted endpoints
		    if (requestURI.equals("/api/v1/auth/login") || requestURI.equals("/api/singlePosts/2") || request.getMethod().equals("GET")) {
		    	filterChain.doFilter(request, response);
		        return;
		    }
		
		if (requestToken != null && requestToken.startsWith("Bearer")) {

			token = requestToken.substring(7);
			try {
				userName = this.jwtTokenHelper.getUsernameFromToken(token);
			} catch (IllegalArgumentException e) {
				log.error("unable to get JWT token", e);
			} catch (ExpiredJwtException e) {
				log.error("JWT token has expired", e);
			} catch (MalformedJwtException e) {
				log.error("Invalid JWT token", e);
			}

		} else {

			log.info("JWT Token does not start with Bearer");
		}
		// Once get the token now validate the token
		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			log.info("inside validate token in filter==:{}",userName);
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
			Boolean validToken = this.jwtTokenHelper.validateToken(token, userDetails);

			if (validToken) {
				// token has matched
               // now do authentication
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

			} else {
				log.info("Token does not matched");
			}

		} else {
			log.info("userName is null or context is not null");
		}
		filterChain.doFilter(request, response);
	}

}
