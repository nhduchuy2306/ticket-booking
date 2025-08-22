package com.gyp.common.configurations;

import javax.crypto.spec.SecretKeySpec;

import com.nimbusds.jose.JWSAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtDecoderEncoderConfiguration {
	@Bean
	public JwtDecoder jwtDecoder(@Value("${jwt.secret.token}") String jwtSecretKey) {
		SecretKeySpec secretKeySpec = new SecretKeySpec(jwtSecretKey.getBytes(), JWSAlgorithm.HS512.getName());
		return NimbusJwtDecoder
				.withSecretKey(secretKeySpec)
				.macAlgorithm(MacAlgorithm.HS512)
				.build();
	}
}
