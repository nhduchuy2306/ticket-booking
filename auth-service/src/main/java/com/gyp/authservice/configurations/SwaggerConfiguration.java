package com.gyp.authservice.configurations;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
	private static final String SECURITY_SCHEME_NAME = "bearerAuth";

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.servers(List.of(
						new Server().url("http://localhost:9000").description("Auth Service"),
						new Server().url("http://172.19.0.6:9000").description("Auth Service - Docker")
				))
				.info(new Info().title("User Service API")
						.version("1.0")
						.description("API Documentation"))
				.components(new Components()
						.addSecuritySchemes(SECURITY_SCHEME_NAME,
								new SecurityScheme()
										.name(SECURITY_SCHEME_NAME)
										.type(SecurityScheme.Type.HTTP)
										.scheme("bearer")
										.bearerFormat("JWT")));
	}

	@Bean
	public OpenApiCustomizer securityOpenApiCustomizer() {
		return openApi -> {
			SecurityRequirement securityItem = new SecurityRequirement().addList(SECURITY_SCHEME_NAME);

			for (Map.Entry<String, PathItem> entry : openApi.getPaths().entrySet()) {
				String path = entry.getKey();

				// Exclude login and register endpoints
				if ("/login".equals(path) || "/register".equals(path)) {
					continue;
				}

				PathItem pathItem = entry.getValue();

				if (pathItem.getGet() != null) {
					pathItem.getGet().addSecurityItem(securityItem);
				}
				if (pathItem.getPost() != null) {
					pathItem.getPost().addSecurityItem(securityItem);
				}
				if (pathItem.getPut() != null) {
					pathItem.getPut().addSecurityItem(securityItem);
				}
				if (pathItem.getDelete() != null) {
					pathItem.getDelete().addSecurityItem(securityItem);
				}
				if (pathItem.getPatch() != null) {
					pathItem.getPatch().addSecurityItem(securityItem);
				}
			}
		};
	}
}
