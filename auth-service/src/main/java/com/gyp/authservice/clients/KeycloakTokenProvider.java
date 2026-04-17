package com.gyp.authservice.clients;

import java.util.Map;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KeycloakTokenProvider {
	private final RestTemplate restTemplate;

	public String getMasterBearerToken() {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("client_id", "admin-cli");
		formData.add("username", "admin");
		formData.add("password", "admin");
		formData.add("grant_type", "password");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
		return Objects.requireNonNull(restTemplate.postForObject(
				"http://localhost:8080/realms/master/protocol/openid-connect/token",
				request,
				Map.class
		)).get("access_token").toString();
	}
}
