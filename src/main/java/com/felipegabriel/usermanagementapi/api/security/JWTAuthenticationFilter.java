package com.felipegabriel.usermanagementapi.api.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

public class JWTAuthenticationFilter extends GenericFilterBean {
	
	public static final String AUTHORIZATION_HEADER = "Authorization";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String jwt = resolveToken(httpServletRequest);

		if (StringUtils.hasText(jwt) && TokenAuthenticationService.validateToken(jwt)) {
			Authentication authentication = TokenAuthenticationService.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(httpServletRequest, response);
		
	}
	
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

}
