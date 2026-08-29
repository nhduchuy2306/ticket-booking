package com.gyp.commonmail.configurations;

import java.util.Properties;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@AutoConfiguration
@ConditionalOnClass(JavaMailSender.class)
@EnableConfigurationProperties(MailProperties.class)
@ComponentScan(basePackages = "com.gyp.commonmail")
public class MailConfiguration {

	@Bean
	@ConditionalOnMissingBean(JavaMailSender.class)
	public JavaMailSender javaMailSender(MailProperties mailProperties) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(mailProperties.getHost());
		mailSender.setPort(mailProperties.getPort());
		mailSender.setUsername(mailProperties.getUsername());
		mailSender.setPassword(mailProperties.getPassword());

		Properties props = mailSender.getJavaMailProperties();
		props.putAll(mailProperties.getProperties());
		if (mailProperties.getDefaultEncoding() != null) {
			mailSender.setDefaultEncoding(mailProperties.getDefaultEncoding().name());
		}

		return mailSender;
	}
}
