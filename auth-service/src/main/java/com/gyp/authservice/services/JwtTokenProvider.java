package com.gyp.authservice.services;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.common.converters.Serialization;
import com.gyp.common.enums.permission.ApplicationPermission;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
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

	public String generateToken(UserAccountResponseDto dto) {
		JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

		JWTClaimsSet jwtClaimsSet = new Builder()
				.subject(dto.getUsername())
				.issuer("auth-service")
				.issueTime(new Date())
				.expirationTime(new Date(
						Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()
				))
				.claim("scope", buildAuthoritiesScope(dto))
				.build();

		Payload payload = new Payload(jwtClaimsSet.toJSONObject());

		JWSObject jwsObject = new JWSObject(jwsHeader, payload);
		try {
			jwsObject.sign(new MACSigner(jwtSecretKey.getBytes()));
			return jwsObject.serialize();
		} catch(JOSEException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public String getUsername(String token) throws ParseException {
		SignedJWT signedJWT = SignedJWT.parse(token);
		return signedJWT.getJWTClaimsSet().getSubject();
	}

	public boolean validateToken(String token) throws JOSEException, ParseException {
		JWSVerifier jwsVerifier = new MACVerifier(jwtSecretKey.getBytes());
		SignedJWT signedJWT = SignedJWT.parse(token);
		Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
		boolean isValid = signedJWT.verify(jwsVerifier);
		return isValid && expirationTime.after(new Date());
	}

	private String buildAuthoritiesScope(UserAccountResponseDto dto) {
		if(dto.getUserGroupResponseDtoList().size() == 1) {
			var firstElement = dto.getUserGroupResponseDtoList().get(0);
			if(BooleanUtils.isTrue(firstElement.getAdministrator())) {
				return ApplicationPermission.ADMIN.getApplicationId();
			}
		}

		Map<String, List<String>> appIdAndActions = new HashMap<>();
		var userGroupList = dto.getUserGroupResponseDtoList();
		userGroupList.forEach(userGroup -> {
			var permissionItems = userGroup.getUserGroupPermissions().getPermissionItems();
			permissionItems.forEach(permissionItem -> {
				var actions = permissionItem.getActions();
				var appId = permissionItem.getApplicationId();
				appIdAndActions.computeIfAbsent(appId, k -> actions.stream().map(Enum::name).toList());
			});
		});

		try {
			return Serialization.serializeToString(appIdAndActions);
		} catch(JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
	}
}

