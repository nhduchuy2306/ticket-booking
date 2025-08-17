package com.gyp.salechannelservice.configurations;

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
						new Server().url("http://localhost:9999/salechannels").description("Sale channel Service"),
						new Server().url("http://localhost:9004").description("Sale channel Service")))
				.info(new Info().title("SaleChannel Service API")
						.version("1.0")
						.description("API Documentation"));
	}
}
