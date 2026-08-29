package com.gyp.common.jwt;

import java.text.ParseException;
import java.util.Date;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.lang3.StringUtils;

public final class JwtTokenProvider {

	private JwtTokenProvider() {
	}

	public static String getUserPayload(String token, String jwtSecretKey) throws JOSEException, ParseException {
		JWSVerifier jwsVerifier = new MACVerifier(jwtSecretKey.getBytes());
		SignedJWT signedJWT = SignedJWT.parse(token);
		Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
		boolean isValid = signedJWT.verify(jwsVerifier);
		if(isValid && expirationTime.after(new Date())) {
			var claims = signedJWT.getJWTClaimsSet().getClaims();
			return claims.get("scope").toString();
		}
		return StringUtils.EMPTY;
	}

	public static String getUserId(String token, String jwtSecretKey) throws JOSEException, ParseException {
		JWSVerifier jwsVerifier = new MACVerifier(jwtSecretKey.getBytes());
		SignedJWT signedJWT = SignedJWT.parse(token);
		Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
		boolean isValid = signedJWT.verify(jwsVerifier);
		if(isValid && expirationTime.after(new Date())) {
			var claims = signedJWT.getJWTClaimsSet().getClaims();
			return claims.get("userId").toString();
		}
		return StringUtils.EMPTY;
	}

	public static String getOrganizationId(String token, String jwtSecretKey) throws JOSEException, ParseException {
		JWSVerifier jwsVerifier = new MACVerifier(jwtSecretKey.getBytes());
		SignedJWT signedJWT = SignedJWT.parse(token);
		Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
		boolean isValid = signedJWT.verify(jwsVerifier);
		if(isValid && expirationTime.after(new Date())) {
			var claims = signedJWT.getJWTClaimsSet().getClaims();
			return claims.get("organizationId").toString();
		}
		return StringUtils.EMPTY;
	}

	public static String getUserSub(String token, String jwtSecretKey) throws JOSEException, ParseException {
		JWSVerifier jwsVerifier = new MACVerifier(jwtSecretKey.getBytes());
		SignedJWT signedJWT = SignedJWT.parse(token);
		Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
		boolean isValid = signedJWT.verify(jwsVerifier);
		if(isValid && expirationTime.after(new Date())) {
			var claims = signedJWT.getJWTClaimsSet().getClaims();
			return claims.get("sub").toString();
		}
		return StringUtils.EMPTY;
	}

	public static boolean validateToken(String token, String jwtSecretKey) throws JOSEException, ParseException {
		JWSVerifier jwsVerifier = new MACVerifier(jwtSecretKey.getBytes());
		SignedJWT signedJWT = SignedJWT.parse(token);
		Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
		boolean isValid = signedJWT.verify(jwsVerifier);
		return isValid && expirationTime.after(new Date());
	}
}
