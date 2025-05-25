package com.gyp.orderservice.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
	private static final String[] PUBLIC_ENDPOINTS = {
			"/v3/api-docs/**", "/v3/api-docs**", "/swagger-ui/**",
			"/swagger-ui.html", "/swagger-resources/**", "/webjars/**"
	};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);
		http.cors(Customizer.withDefaults());
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authorizeHttpRequests(request ->
				request.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
						.anyRequest().permitAll()
		);
		return http.build();
	}
}
