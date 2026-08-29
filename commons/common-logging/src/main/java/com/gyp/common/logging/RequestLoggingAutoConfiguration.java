package com.gyp.common.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for request logging.
 *
 * <p>Automatically detects whether the application is Servlet-based (MVC)
 * or Reactive-based (WebFlux) and registers the appropriate filter.</p>
 */
@Slf4j
@AutoConfiguration
public class RequestLoggingAutoConfiguration {

	/**
	 * Configuration for Servlet/MVC applications.
	 */
	@Configuration
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	static class ServletLoggingConfiguration {

		@Bean
		public RequestIdFilter requestIdFilter() {
			log.info("Registering Servlet RequestIdFilter for request tracing");
			return new RequestIdFilter();
		}

		@Bean
		@ConditionalOnClass(name = "feign.RequestInterceptor")
		public FeignRequestIdInterceptor feignRequestIdInterceptor() {
			log.info("Registering FeignRequestIdInterceptor for service-to-service tracing");
			return new FeignRequestIdInterceptor();
		}
	}

	/**
	 * Configuration for Reactive/WebFlux applications (e.g., API Gateway).
	 */
	@Configuration
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	static class ReactiveLoggingConfiguration {

		@Bean
		public WebFluxRequestIdFilter webFluxRequestIdFilter() {
			log.info("Registering WebFlux RequestIdFilter for request tracing");
			return new WebFluxRequestIdFilter();
		}
	}
}
