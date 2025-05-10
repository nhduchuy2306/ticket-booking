package com.gyp.gateway.configuration;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
	@Bean
	public GroupedOpenApi authServiceApi() {
		return GroupedOpenApi.builder()
				.group("auth-service")
				.pathsToMatch("/auths/**")
				.addOpenApiCustomizer(openApi -> openApi.info(new Info().title("User Service")))
				.build();
	}

	@Bean
	public GroupedOpenApi eventServiceApi() {
		return GroupedOpenApi.builder()
				.group("event-service")
				.pathsToMatch("/events/**")
				.addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Event Service")))
				.build();
	}

	@Bean
	public GroupedOpenApi ticketServiceApi() {
		return GroupedOpenApi.builder()
				.group("ticket-service")
				.pathsToMatch("/tickets/**")
				.addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Ticket Service")))
				.build();
	}

	@Bean
	public GroupedOpenApi saleChannelServiceApi() {
		return GroupedOpenApi.builder()
				.group("salechannel-service")
				.pathsToMatch("/salechannels/**")
				.addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Sale Channel Service")))
				.build();
	}

	@Bean
	public GroupedOpenApi orderServiceApi() {
		return GroupedOpenApi.builder()
				.group("order-service")
				.pathsToMatch("/orders/**")
				.addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Order Service")))
				.build();
	}
}
