package com.gyp.authservice.configurations;

import com.gyp.authservice.clients.KeycloakTokenProvider;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class FeignClientConfiguration {
	private final ObjectFactory<HttpMessageConverters> messageConverters;

	@Bean
	public Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}

	@Bean
	public RequestInterceptor requestInterceptor(KeycloakTokenProvider keycloakTokenProvider) {
		return requestTemplate -> {
			if(!requestTemplate.url().contains("/protocol/openid-connect/token")) {
				String token = keycloakTokenProvider.getMasterBearerToken();
				requestTemplate.header("Authorization", "Bearer " + token);
			}
		};
	}

	@Bean
	public Encoder feignEncoder() {
		return new SpringFormEncoder(new SpringEncoder(messageConverters));
	}

	@Bean
	public Decoder feignDecoder() {
		return new SpringDecoder(messageConverters);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
