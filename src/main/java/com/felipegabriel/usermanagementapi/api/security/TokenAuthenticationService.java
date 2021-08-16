package com.felipegabriel.usermanagementapi.api.security;

import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.ExpiredJwtException;
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
	private static final Long EXPIRATION_TIME = 3_600_000L;

	public static void addAuthentication(HttpServletResponse response, String username) {
		String token = Jwts.builder().setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();

		response.addHeader(HEADER, TOKEN_PREFIX + " " + token);
		response.addHeader("access-control-expose-headers", HEADER);
	}

	public static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER);

		if (token != null) {
			String user = null;

			try {
				user = Jwts.parser()
						.setSigningKey(SECRET)
						.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
						.getBody()
						.getSubject();
			} catch (ExpiredJwtException e) {
				System.err.println("Expired token.");
			} catch (Exception e) {
				System.err.print("Error on token conversion.");
			}
			
			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
			}
		}
		
		return null;
		
	}
}
