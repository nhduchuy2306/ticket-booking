package com.gyp.common.logging;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * WebFlux filter for reactive services (e.g., API Gateway).
 *
 * <p>Generates a unique requestId for each incoming request,
 * propagates it via Reactor Context for MDC-like behavior
 * in reactive pipelines.</p>
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebFluxRequestIdFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		long startTime = System.currentTimeMillis();

		ServerHttpRequest request = exchange.getRequest();
		HttpHeaders headers = request.getHeaders();

		// Generate or reuse request ID
		String requestId = headers.getFirst(LoggingConstants.HEADER_REQUEST_ID);
		if(requestId == null || requestId.isBlank()) {
			requestId = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
		}

		// Get or generate correlation ID
		String correlationId = headers.getFirst(LoggingConstants.HEADER_CORRELATION_ID);
		if(correlationId == null || correlationId.isBlank()) {
			correlationId = requestId;
		}

		final String finalRequestId = requestId;
		final String finalCorrelationId = correlationId;

		// Add request ID to response headers
		ServerHttpResponse response = exchange.getResponse();
		response.getHeaders().set(LoggingConstants.HEADER_REQUEST_ID, requestId);
		response.getHeaders().set(LoggingConstants.HEADER_CORRELATION_ID, correlationId);

		// Mutate request to include IDs for downstream services
		ServerHttpRequest mutatedRequest = request.mutate()
				.header(LoggingConstants.HEADER_REQUEST_ID, requestId)
				.header(LoggingConstants.HEADER_CORRELATION_ID, correlationId)
				.build();

		// Set MDC for the current thread
		MDC.put(LoggingConstants.MDC_REQUEST_ID, requestId);
		MDC.put(LoggingConstants.MDC_CORRELATION_ID, correlationId);

		String path = request.getPath().value();
		String method = request.getMethod() != null ? request.getMethod().name() : "UNKNOWN";

		// Skip logging for actuator endpoints
		if(path.startsWith("/actuator") || path.equals("/health")) {
			return chain.filter(exchange.mutate().request(mutatedRequest).build());
		}

		log.info(">>> [{}] {} (from: {})", method, path, request.getRemoteAddress());

		return chain.filter(exchange.mutate().request(mutatedRequest).build())
				.doFinally(signalType -> {
					long duration = System.currentTimeMillis() - startTime;
					MDC.put(LoggingConstants.MDC_REQUEST_ID, finalRequestId);
					MDC.put(LoggingConstants.MDC_CORRELATION_ID, finalCorrelationId);
					log.info("<<< [{}] {} | status={} | duration={}ms",
							method, path,
							response.getStatusCode() != null ? response.getStatusCode().value() : "N/A",
							duration);
					MDC.remove(LoggingConstants.MDC_REQUEST_ID);
					MDC.remove(LoggingConstants.MDC_CORRELATION_ID);
				})
				.contextWrite(Context.of(
						LoggingConstants.MDC_REQUEST_ID, finalRequestId,
						LoggingConstants.MDC_CORRELATION_ID, finalCorrelationId
				));
	}
}
