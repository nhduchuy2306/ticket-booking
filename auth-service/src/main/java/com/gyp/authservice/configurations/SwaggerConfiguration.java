package com.gyp.authservice.configurations;

import java.util.List;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.servers(List.of(
						new Server().url("http://localhost:9000").description("Auth Service"),
						new Server().url("http://172.19.0.6:9000").description("Auth Service - Docker")
				))
				.info(new Info().title("User Service API")
						.version("1.0")
						.description("API Documentation"));
	}
}
