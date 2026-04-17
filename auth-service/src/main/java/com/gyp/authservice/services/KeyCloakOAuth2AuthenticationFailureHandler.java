package com.gyp.authservice.services;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KeyCloakOAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException {
		String errorMessage = exception.getMessage() == null ? "OAuth2 login failed" : exception.getMessage();
		String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/iam/login")
				.queryParam("oauth2Error", errorMessage)
				.build()
				.toUriString();
		response.sendRedirect(redirectUrl);
	}
}
