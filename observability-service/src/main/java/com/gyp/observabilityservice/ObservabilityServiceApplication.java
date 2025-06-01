package com.gyp.observabilityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(CorsConfiguration.class)
public class ObservabilityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObservabilityServiceApplication.class, args);
	}

}
