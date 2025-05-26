package com.gyp.gateway.configuration;

import java.util.Arrays;
import java.util.List;

import com.gyp.common.constants.AppConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfiguration {
	@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowedMethods(List.of("*"));
		configuration.setAllowCredentials(false);
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);

		return new CorsWebFilter(urlBasedCorsConfigurationSource);
	}

	@Bean
	public WebFilter corsGatewayFilter() {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			if(CorsUtils.isCorsRequest(request)) {
				ServerHttpResponse response = exchange.getResponse();
				HttpHeaders headers = response.getHeaders();
				headers.add("Access-Control-Allow-Origin", "*");
				headers.add("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
				headers.add("Access-Control-Allow-Headers", "*");
				headers.add("Access-Control-Max-Age", "3600");

				if(HttpMethod.OPTIONS.equals(request.getMethod())) {
					response.setStatusCode(HttpStatus.OK);
					return Mono.empty();
				}
			}
			return chain.filter(exchange);
		};
	}

	@Bean
	public WebClient webClient() {
		return WebClient.builder()
				.baseUrl(AppConstants.AUTH_SERVICE_API)
				.build();
	}
}
