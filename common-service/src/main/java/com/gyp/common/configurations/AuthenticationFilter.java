package com.gyp.common.configurations;

import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.gyp.common.constants.AppConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String payload = request.getHeader(AppConstants.APP_HEADER_USER_PAYLOAD);
		String subject = request.getHeader(AppConstants.APP_HEADER_USER_SUBJECT);
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if(StringUtils.isNotEmpty(payload) && StringUtils.isNotEmpty(authorizationHeader)) {

			String token = extractToken(authorizationHeader);

			Collection<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(payload));

			Map<String, Object> claims = Map.of("payload", payload, "sub", subject);

			Jwt jwt = new Jwt(token, Instant.now(), Instant.now().plusSeconds(3600), Map.of("alg", "none"), claims);

			JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	private String extractToken(String authorizationHeader) {
		if(authorizationHeader.startsWith(AppConstants.APP_TOKEN_PREFIX)) {
			return authorizationHeader.substring(7);
		}
		return authorizationHeader;
	}
}


