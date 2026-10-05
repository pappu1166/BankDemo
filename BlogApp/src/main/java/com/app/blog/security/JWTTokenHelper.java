package com.app.blog.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTTokenHelper {

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // 5 hours

	@Value("${jwt.secret}") // Load secret key from application.properties
	private String secret;

	// Retrieve username from JWT token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	// Retrieve expiration date from JWT token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	// Retrieve specific claim from token
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// Retrieve all claims from JWT token
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token).getBody();
	}

	// Check if token is expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// Generate JWT token for user
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}

	// Create JWT token
	private String doGenerateToken(Map<String, Object> claims, String userName) {
		return Jwts.builder()
				.claims(claims)
				.subject(userName)
				.header().empty().add("typ", "JWT").and()
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(getSigingkKey()) // Corrected signing method
				.compact();
	}

	// Validate JWT token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	public SecretKey getSigingkKey() {
		return Keys.hmacShaKeyFor(secret.getBytes());
	}
}
