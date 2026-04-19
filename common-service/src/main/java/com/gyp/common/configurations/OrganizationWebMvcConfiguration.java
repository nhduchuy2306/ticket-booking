package com.gyp.common.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class OrganizationWebMvcConfiguration implements WebMvcConfigurer {
	private final OrganizationInterceptor organizationInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(organizationInterceptor)
				.addPathPatterns("/**");
	}
}

