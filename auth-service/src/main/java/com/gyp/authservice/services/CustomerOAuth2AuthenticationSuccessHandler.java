package com.gyp.authservice.services;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.gyp.authservice.dtos.customer.CustomerAuthResponseDto;
import com.gyp.authservice.entities.CustomerEntity;
import com.gyp.authservice.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class CustomerOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private final CustomerRepository customerRepository;
	private final CustomerAuthService customerAuthService;

	@Value("${customer.frontend.oauth2-callback-url}")
	private String oauth2CallbackUrl;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		OidcUser oidcUser = (OidcUser)authentication.getPrincipal();
		String providerId = oidcUser.getSubject();
		CustomerEntity customerEntity = customerRepository.findByProviderAndProviderId("google", providerId)
				.orElseThrow(() -> new IllegalStateException("Customer not found after OAuth2 login"));

		CustomerAuthResponseDto authResponseDto = customerAuthService.issueTokens(customerEntity);
		String redirectUrl = UriComponentsBuilder.fromUriString(oauth2CallbackUrl)
				.queryParam("accessToken", authResponseDto.getAccessToken())
				.queryParam("refreshToken", authResponseDto.getRefreshToken())
				.queryParam("expiresIn", authResponseDto.getExpiresIn())
				.build()
				.toUriString();
		response.sendRedirect(redirectUrl);
	}
}