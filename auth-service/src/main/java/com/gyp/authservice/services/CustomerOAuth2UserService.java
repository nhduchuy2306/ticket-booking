package com.gyp.authservice.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.gyp.authservice.entities.CustomerEntity;
import com.gyp.authservice.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {
	private static final String GOOGLE_PROVIDER = "google";

	private final CustomerRepository customerRepository;

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) {
		OidcIdToken idToken = userRequest.getIdToken();
		String providerId = idToken.getSubject();
		String email = idToken.getClaimAsString("email");
		String name = idToken.getClaimAsString("name");

		if(email == null || email.isBlank()) {
			throw oauth2AuthenticationException("Google account did not return an email address");
		}

		CustomerEntity customerEntity = customerRepository.findByProviderAndProviderId(GOOGLE_PROVIDER, providerId)
				.orElseGet(() -> createGoogleCustomer(providerId, email, name));

		if(customerEntity.getEmail() != null && !customerEntity.getEmail().equalsIgnoreCase(email)) {
			customerEntity.setEmail(email);
		}
		if(name != null && !name.isBlank() && !name.equals(customerEntity.getName())) {
			customerEntity.setName(name);
		}
		customerEntity.setChangeTimestamp(LocalDateTime.now());
		customerRepository.save(customerEntity);

		OidcUserInfo userInfo = new OidcUserInfo(idToken.getClaims());
		return new DefaultOidcUser(List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")), idToken, userInfo, "sub");
	}

	private CustomerEntity createGoogleCustomer(String providerId, String email, String name) {
		if(customerRepository.existsByEmail(email)) {
			throw oauth2AuthenticationException("An account with this email already exists");
		}

		CustomerEntity customerEntity = CustomerEntity.builder()
				.id(UUID.randomUUID().toString())
				.name(name)
				.email(email)
				.provider(GOOGLE_PROVIDER)
				.providerId(providerId)
				.build();
		customerEntity.setCreateTimestamp(LocalDateTime.now());
		customerEntity.setChangeTimestamp(LocalDateTime.now());
		return customerRepository.save(customerEntity);
	}

	private OAuth2AuthenticationException oauth2AuthenticationException(String message) {
		log.warn(message);
		return new OAuth2AuthenticationException(new OAuth2Error("customer_oauth2_error", message, null));
	}
}