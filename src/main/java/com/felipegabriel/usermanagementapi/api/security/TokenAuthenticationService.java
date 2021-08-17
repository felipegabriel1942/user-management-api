package com.felipegabriel.usermanagementapi.api.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenAuthenticationService {

	private static final String SECRET = "19421945";
	private static final String TOKEN_PREFIX = "Bearer";
	private static final String HEADER = "Authorization";
	private static final String AUTHORITIES_KEY = "auth";
	private static final Long EXPIRATION_TIME = 3_600_000L;

	public static void addAuthentication(HttpServletResponse response, Authentication authentication) {
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		String token = Jwts.builder()
				.setSubject(authentication.getName())
				.claim(AUTHORITIES_KEY, authorities)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();

		response.addHeader(HEADER, TOKEN_PREFIX + " " + token);
		response.addHeader("access-control-expose-headers", HEADER);
	}

	public static Authentication getAuthentication(String token) {
		if (token != null) {
			try {
				
				Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();

				Collection<? extends GrantedAuthority> authorities = Arrays
						.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
						.filter(auth -> !auth.trim().isEmpty())
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());

				User principal = new User(claims.getSubject(), "", authorities);

				if (principal != null) {
					return new UsernamePasswordAuthenticationToken(principal, token, authorities);
				}
			} catch (ExpiredJwtException e) {
				System.err.println("Expired token.");
			} catch (Exception e) {
				System.err.print("Error on token conversion.");
			}
		}
		
		return null;
		
	}
	
	public static boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(SECRET).parseClaimsJws(authToken);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			System.err.println("Invalid JWT token.");
		}
		return false;
	}
}
