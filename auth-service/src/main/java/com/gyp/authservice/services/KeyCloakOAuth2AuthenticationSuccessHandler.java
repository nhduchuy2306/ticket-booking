package com.gyp.authservice.services;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.authservice.entities.UserGroupEntity;
import com.gyp.authservice.mappers.UserGroupMapper;
import com.gyp.authservice.repositories.UserAccountRepository;
import com.gyp.authservice.repositories.UserGroupRepository;
import com.gyp.common.enums.auth.RealmTypeEnum;
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
	private final JwtTokenProvider jwtTokenProvider;
	private final UserAccountRepository userAccountRepository;
	private final UserGroupRepository userGroupRepository;
	private final UserGroupMapper userGroupMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken)authentication;

		OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
				oauthToken.getAuthorizedClientRegistrationId(),
				oauthToken.getName()
		);

		var principal = authentication.getPrincipal();
		if(principal instanceof OidcUser oidcUser) {
			Optional<UserAccountEntity> userAccount = userAccountRepository.findByEmailAndRealmType(
					oidcUser.getEmail(), RealmTypeEnum.GYP_KEYCLOAK_REALM);
			List<UserGroupEntity> userGroupEntityList = userGroupRepository.findByUserAccountEntityList(
					userAccount.map(List::of).orElse(List.of()));
			log.info("UserGroupEntityList: {}", userGroupEntityList);

			String accessToken = client.getAccessToken().getTokenValue();
			String idToken = oidcUser.getIdToken().getTokenValue();

			log.info("ACCESS TOKEN: {}", accessToken);
			log.info("ID TOKEN: {}", idToken);
		}
	}
}
