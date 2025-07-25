package com.gyp.ticketservice;

import com.gyp.common.configurations.CorsConfiguration;
import com.gyp.ticketservice.messages.grpcs.EventServiceGrpcClient;
import io.lettuce.core.protocol.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableCaching
@SpringBootApplication
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@ComponentScan(basePackages = { "com.gyp.common", "com.gyp.ticketservice" })
@EnableConfigurationProperties(CorsConfiguration.class)
public class TicketServiceApplication implements CommandLineRunner {

	@Autowired
	private EventServiceGrpcClient eventServiceGrpcClient;

	public static void main(String[] args) {
		SpringApplication.run(TicketServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			var data = eventServiceGrpcClient.getAllEvents();
			System.out.println("Fetched events: " + data);
		} catch (Exception e) {
			System.err.println("Error fetching event: " + e.getMessage());
		}
	}
}
