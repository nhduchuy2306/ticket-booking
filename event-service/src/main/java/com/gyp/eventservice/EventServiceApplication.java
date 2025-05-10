package com.gyp.eventservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@ComponentScan(basePackages = { "com.gyp.common.*", "com.gyp.eventservice.*" })
@SpringBootApplication
public class EventServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventServiceApplication.class, args);
	}

}
