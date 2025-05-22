package com.gyp.salechannelservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@ComponentScan(basePackages = { "com.gyp.salechannelservice", "com.gyp.common" })
@SpringBootApplication
public class SalechannelServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalechannelServiceApplication.class, args);
	}

}
