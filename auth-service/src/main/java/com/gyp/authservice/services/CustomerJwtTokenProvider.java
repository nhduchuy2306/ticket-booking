package com.gyp.authservice.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.gyp.authservice.entities.CustomerEntity;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerJwtTokenProvider {
	private static final long ACCESS_TOKEN_EXPIRATION_MINUTES = 15L;

	@Value("${jwt.secret.token}")
	private String jwtSecretKey;

	public String generateAccessToken(CustomerEntity customerEntity) {
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.subject(customerEntity.getId())
				.issuer("auth-service")
				.issueTime(new Date())
				.expirationTime(new Date(Instant.now().plus(ACCESS_TOKEN_EXPIRATION_MINUTES, ChronoUnit.MINUTES).toEpochMilli()))
				.claim("email", customerEntity.getEmail())
				.claim("provider", customerEntity.getProvider())
				.claim("role", "CUSTOMER")
				.build();
		return signJwt(claimsSet);
	}

	private String signJwt(JWTClaimsSet claimsSet) {
		JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
		Payload payload = new Payload(claimsSet.toJSONObject());
		JWSObject jwsObject = new JWSObject(jwsHeader, payload);
		try {
			jwsObject.sign(new MACSigner(jwtSecretKey.getBytes()));
			return jwsObject.serialize();
		} catch(JOSEException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public long getAccessTokenExpiresInSeconds() {
		return ACCESS_TOKEN_EXPIRATION_MINUTES * 60L;
	}
}