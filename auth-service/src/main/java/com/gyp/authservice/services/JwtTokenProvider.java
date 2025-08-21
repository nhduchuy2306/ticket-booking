package com.gyp.authservice.services;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.common.converters.Serialization;
import com.gyp.common.enums.permission.ApplicationPermission;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtTokenProvider {

	@Value("${jwt.secret.token}")
	private String jwtSecretKey;

	public static final String AUTH_CODE = "auth_code";

	public String generateTokenWithPermissions(UserAccountResponseDto dto) {
		try {
			// Get user permissions from database
			Map<String, List<String>> appIdAndActions = getAppIdAndActions(dto);

			// Build JWT claims with permissions
			JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
					.subject(dto.getId())
					.issuer("auth-service")
					.expirationTime(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
					.issueTime(new Date());

			// Add permissions to JWT
			if(!appIdAndActions.isEmpty()) {
				claimsBuilder.claim("permissions", appIdAndActions);
			}

			// Add other user info
			claimsBuilder.claim("organizationId", dto.getOrganizationId());
			claimsBuilder.claim("userId", dto.getId());

			JWTClaimsSet claims = claimsBuilder.build();

			// Sign and return token
			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS512), claims);

			signedJWT.sign(new MACSigner(jwtSecretKey.getBytes()));
			return signedJWT.serialize();

		} catch(Exception e) {
			throw new RuntimeException("Error generating JWT token", e);
		}
	}

	public String generateToken(UserAccountResponseDto dto) {
		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
				.subject(dto.getUsername())
				.issuer("auth-service")
				.issueTime(new Date())
				.expirationTime(new Date(
						Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()
				))
				.claim("scope", buildAuthoritiesScope(dto))
				.claim("organizationId", dto.getOrganizationId())
				.claim("userId", dto.getId())
				.build();
		return signJwt(jwtClaimsSet);
	}

	public String generateAuthCode(UserAccountResponseDto user, String clientId) {
		JWTClaimsSet jwtClaimsSet = new Builder()
				.subject(user.getId())
				.claim("clientId", clientId)
				.claim("type", AUTH_CODE)
				.issueTime(new Date())
				.expirationTime(new Date(Instant.now().plusSeconds(300).toEpochMilli()))
				.build();
		return signJwt(jwtClaimsSet);
	}

	private String signJwt(JWTClaimsSet claims) {
		JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
		Payload payload = new Payload(claims.toJSONObject());
		JWSObject jwsObject = new JWSObject(jwsHeader, payload);
		try {
			jwsObject.sign(new MACSigner(jwtSecretKey.getBytes()));
			return jwsObject.serialize();
		} catch(JOSEException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public Object getClaim(String token, String key) throws JOSEException, ParseException {
		SignedJWT signedJWT = SignedJWT.parse(token);
		if(!signedJWT.verify(new MACVerifier(jwtSecretKey.getBytes()))) {
			throw new IllegalArgumentException("Invalid token signature");
		}
		var jwtClaimsSet = signedJWT.getJWTClaimsSet();
		if(jwtClaimsSet == null) {
			throw new IllegalArgumentException("JWT claims set is null");
		}
		return jwtClaimsSet.getClaim(key);
	}

	private String buildAuthoritiesScope(UserAccountResponseDto dto) {
		if(dto.getUserGroupList().size() == 1) {
			var firstElement = dto.getUserGroupList().getFirst();
			if(BooleanUtils.isTrue(firstElement.getAdministrator())) {
				return ApplicationPermission.ADMIN.getApplicationId();
			}
		}

		Map<String, List<String>> appIdAndActions = getAppIdAndActions(dto);
		try {
			return Serialization.serializeToString(appIdAndActions);
		} catch(JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private Map<String, List<String>> getAppIdAndActions(UserAccountResponseDto dto) {
		Map<String, List<String>> appIdAndActions = new HashMap<>();
		var userGroupList = dto.getUserGroupList();
		if(userGroupList.size() == 1) {
			var firstElement = userGroupList.getFirst();
			if(BooleanUtils.isTrue(firstElement.getAdministrator())) {
				appIdAndActions.computeIfAbsent(ApplicationPermission.ADMIN.getApplicationId(), k -> List.of());
			}
		}

		userGroupList.forEach(userGroup -> {
			var permissionItems = userGroup.getUserGroupPermissions().getPermissionItems();
			permissionItems.forEach(permissionItem -> {
				var actions = permissionItem.getActions();
				var appId = permissionItem.getApplicationId();
				appIdAndActions.computeIfAbsent(appId, k -> actions.stream().map(Enum::name).toList());
			});
		});
		return appIdAndActions;
	}
}

