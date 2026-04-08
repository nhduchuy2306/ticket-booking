package com.gyp.eventservice;

import com.gyp.common.configurations.CorsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableKafka
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@EnableScheduling
@ComponentScan(basePackages = { "com.gyp.common.*", "com.gyp.eventservice.*" })
@SpringBootApplication
@EnableConfigurationProperties(CorsConfiguration.class)
public class EventServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventServiceApplication.class, args);
	}

}
