package com.gyp.authservice.configurations;

import com.gyp.authservice.services.CustomerOAuth2AuthenticationFailureHandler;
import com.gyp.authservice.services.CustomerOAuth2AuthenticationSuccessHandler;
import com.gyp.authservice.services.CustomerOAuth2UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
	private static final String[] PUBLIC_ENDPOINTS = {
			"/login", "/register", "/token", "/useraccounts/**", "/v3/api-docs/**", "/v3/api-docs**",
			"/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**",
			"/iam/oauth/login",
	};

	@Bean
	@Order(1)
	public SecurityFilterChain customerSecurityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder,
			CustomerOAuth2UserService customerOAuth2UserService,
			CustomerOAuth2AuthenticationSuccessHandler successHandler,
			CustomerOAuth2AuthenticationFailureHandler failureHandler) throws Exception {
		http.securityMatcher("/customer/oauth2/authorize/**", "customer/oauth2/callback/**");
		http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable);
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
		http.authorizeHttpRequests(request -> request.anyRequest().permitAll());
		http.oauth2Login(oauth2 -> oauth2
				.authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
						.baseUri("/customer/oauth2/authorize/**"))
				.redirectionEndpoint(redirectionEndpoint -> redirectionEndpoint
						.baseUri("/customer/oauth2/callback/**"))
				.userInfoEndpoint(userInfo -> userInfo.oidcUserService(customerOAuth2UserService))
				.successHandler(successHandler)
				.failureHandler(failureHandler));
		http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder)));
		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable);
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authorizeHttpRequests(request ->
				request.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
						.anyRequest().authenticated());

		http.oauth2ResourceServer(oauth2 ->
				oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder)));

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
}
