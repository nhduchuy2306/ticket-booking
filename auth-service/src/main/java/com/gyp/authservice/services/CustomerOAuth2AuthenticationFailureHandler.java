package com.gyp.authservice.services;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class CustomerOAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Value("${customer.frontend.login-url}")
	private String loginUrl;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		String errorMessage = exception.getMessage() == null ? "OAuth2 login failed" : exception.getMessage();
		String redirectUrl = UriComponentsBuilder.fromUriString(loginUrl)
				.queryParam("oauth2Error", errorMessage)
				.build()
				.toUriString();
		response.sendRedirect(redirectUrl);
	}
}