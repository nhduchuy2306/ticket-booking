package com.gyp.authservice.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeyCloakOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private final OAuth2AuthorizedClientService authorizedClientService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken)authentication;

		OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
				oauthToken.getAuthorizedClientRegistrationId(),
				oauthToken.getName()
		);

		String accessToken = client.getAccessToken().getTokenValue();
		String idToken = ((OidcUser)authentication.getPrincipal())
				.getIdToken()
				.getTokenValue();

		log.info("ACCESS TOKEN: {}", accessToken);
		log.info("ID TOKEN: {}", idToken);
	}
}
