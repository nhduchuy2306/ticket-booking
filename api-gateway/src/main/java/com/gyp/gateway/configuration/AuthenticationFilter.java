package com.gyp.gateway.configuration;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyp.common.constants.AppConstants;
import com.gyp.common.dtos.ApiResponse;
import com.gyp.common.jwt.JwtTokenProvider;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

	private final ObjectMapper objectMapper;

	private String[] publicEndpoints = { "/auths/login", "/auths/logout", "^/[^/]+/v3/api-docs$" };

	@Value("${jwt.secret.token}")
	private String jwtSecretKey;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		try {
			if(isMatchPublicEndpoint(exchange.getRequest())) {
				return chain.filter(exchange);
			}

			List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
			if(CollectionUtils.isEmpty(authHeader)) {
				return unauthenticated(exchange.getResponse());
			}

			String token = authHeader.getFirst().replace(AppConstants.APP_TOKEN_PREFIX, "");
			boolean isValid = JwtTokenProvider.validateToken(token, jwtSecretKey);
			if(!isValid) {
				return unauthenticated(exchange.getResponse());
			}

			String payload = JwtTokenProvider.getUserPayload(token, jwtSecretKey);
			String subject = JwtTokenProvider.getUserSub(token, jwtSecretKey);
			String userId = JwtTokenProvider.getUserId(token, jwtSecretKey);
			String organizationId = JwtTokenProvider.getOrganizationId(token, jwtSecretKey);

			ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
					.header(HttpHeaders.AUTHORIZATION, AppConstants.APP_TOKEN_PREFIX + token)
					.header(AppConstants.APP_HEADER_USER_PAYLOAD, AppConstants.APP_HEADER_SCOPE + payload)
					.header(AppConstants.APP_HEADER_USER_SUBJECT, subject)
					.header(AppConstants.APP_HEADER_USER_ID, userId)
					.header(AppConstants.APP_HEADER_ORGANIZATION_ID, organizationId)
					.build();
			ServerWebExchange mutatedExchange = exchange.mutate()
					.request(mutatedRequest)
					.build();

			return chain.filter(mutatedExchange);
		} catch(JOSEException | ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public int getOrder() {
		return -1;
	}

	private boolean isMatchPublicEndpoint(ServerHttpRequest request) {
		return Arrays.stream(publicEndpoints)
				.anyMatch(s -> request.getURI().getPath().matches(s));
	}

	private boolean isContainPublicEndpoint(ServerHttpRequest request) {
		return Arrays.stream(publicEndpoints)
				.anyMatch(s -> request.getURI().getPath().contains(s));
	}

	private Mono<Void> unauthenticated(ServerHttpResponse response) {
		ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", null);
		String body;
		try {
			body = objectMapper.writeValueAsString(apiResponse);
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
	}
}
