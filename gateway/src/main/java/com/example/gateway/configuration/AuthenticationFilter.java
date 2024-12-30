package com.example.gateway.configuration;

import java.util.Arrays;
import java.util.List;

import com.example.common.constants.AppConstants;
import com.example.common.dto.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

	private String[] publicEndpoints = {};

	@Value("${app.api-prefix}")
	private String apiPrefix;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("Enter authentication filter....");
		if(isPublicEndpoint(exchange.getRequest())) {
			return chain.filter(exchange);
		}

		// Get Token from Authorization header
		List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
		if(CollectionUtils.isEmpty(authHeader)) {
			return unauthenticated(exchange.getResponse());
		}

		String token = authHeader.getFirst().replace(AppConstants.APP_TOKEN_PREFIX, "");
		log.info("Token: {}", token);

		// TODO: Implement call api to validate token

		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return -1;
	}

	private boolean isPublicEndpoint(ServerHttpRequest request) {
		return Arrays.stream(publicEndpoints)
				.anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
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
