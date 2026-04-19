package com.gyp.authservice.services;

import java.time.LocalDateTime;
import java.util.List;

import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.authservice.entities.UserGroupEntity;
import com.gyp.authservice.repositories.UserAccountRepository;
import com.gyp.authservice.repositories.UserGroupRepository;
import com.gyp.common.enums.auth.RealmTypeEnum;
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
public class KeyCloakOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {
	private final UserAccountRepository userAccountRepository;
	private final UserGroupRepository userGroupRepository;

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) {
		OidcIdToken idToken = userRequest.getIdToken();
		String email = idToken.getClaimAsString("email");
		String name = idToken.getClaimAsString("name");

		if(email == null || email.isBlank()) {
			throw oauth2AuthenticationException("Keycloak account did not return an email address");
		}

		UserAccountEntity userAccountEntity = userAccountRepository.findByEmailAndName(email, name)
				.orElseGet(() -> createKeyCloakCustomer(email, name));
		List<SimpleGrantedAuthority> authorities = List.of();
		List<UserGroupEntity> userGroupEntityList = userGroupRepository.findByUserAccountEntityList(
				List.of(userAccountEntity));

		if(userGroupEntityList != null && !userGroupEntityList.isEmpty()) {
			authorities = userGroupEntityList.stream()
					.map(group -> new SimpleGrantedAuthority(group.getName().toUpperCase()))
					.toList();
		}

		userAccountEntity.setChangeTimestamp(LocalDateTime.now());
		userAccountRepository.save(userAccountEntity);

		OidcUserInfo userInfo = new OidcUserInfo(idToken.getClaims());
		return new DefaultOidcUser(authorities, idToken, userInfo, "sub");
	}

	private UserAccountEntity createKeyCloakCustomer(String email, String name) {
		if(userAccountRepository.existsByEmail(email)) {
			throw oauth2AuthenticationException("An account with this email already exists");
		}

		UserAccountEntity customerEntity = UserAccountEntity.builder()
				.name(name)
				.email(email)
				.realmType(RealmTypeEnum.GYP_KEYCLOAK_REALM)
				.build();
		customerEntity.setCreateTimestamp(LocalDateTime.now());
		customerEntity.setChangeTimestamp(LocalDateTime.now());
		return userAccountRepository.save(customerEntity);
	}

	private OAuth2AuthenticationException oauth2AuthenticationException(String message) {
		log.warn(message);
		return new OAuth2AuthenticationException(new OAuth2Error("customer_oauth2_error", message, null));
	}
}