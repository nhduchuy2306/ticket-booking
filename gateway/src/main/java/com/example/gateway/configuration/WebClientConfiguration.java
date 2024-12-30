package com.example.gateway.configuration;

import java.util.List;

import com.example.common.constants.AppConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
	@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedOrigins(List.of("*"));
		corsConfiguration.setAllowedHeaders(List.of("*"));
		corsConfiguration.setAllowedMethods(List.of("*"));

		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

		return new CorsWebFilter(urlBasedCorsConfigurationSource);
	}

	@Bean
	public WebClient webClient() {
		return WebClient.builder()
				.baseUrl(AppConstants.AUTH_SERVICE_API)
				.build();
	}
}
