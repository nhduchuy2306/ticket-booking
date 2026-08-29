package com.gyp.common.logging;

import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Servlet filter that generates a unique requestId for each incoming request
 * and adds it to the MDC for structured logging.
 *
 * <p>If the incoming request already has an {@code X-Request-ID} header,
 * it will be reused. Otherwise, a new UUID is generated.</p>
 *
 * <p>The requestId is also propagated in the response header
 * {@code X-Request-ID} for client-side correlation.</p>
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestIdFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		long startTime = System.currentTimeMillis();

		// Generate or reuse request ID
		String requestId = request.getHeader(LoggingConstants.HEADER_REQUEST_ID);
		if(requestId == null || requestId.isBlank()) {
			requestId = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
		}

		// Get or generate correlation ID
		String correlationId = request.getHeader(LoggingConstants.HEADER_CORRELATION_ID);
		if(correlationId == null || correlationId.isBlank()) {
			correlationId = requestId;
		}

		// Put into MDC
		MDC.put(LoggingConstants.MDC_REQUEST_ID, requestId);
		MDC.put(LoggingConstants.MDC_CORRELATION_ID, correlationId);

		// Add to response headers
		response.setHeader(LoggingConstants.HEADER_REQUEST_ID, requestId);
		response.setHeader(LoggingConstants.HEADER_CORRELATION_ID, correlationId);

		try {
			// Log request start
			log.info(">>> [{}] {} {} (from: {})",
					request.getMethod(),
					request.getRequestURI(),
					request.getQueryString() != null ? "?" + request.getQueryString() : "",
					request.getRemoteAddr());

			filterChain.doFilter(request, response);

		} finally {
			long duration = System.currentTimeMillis() - startTime;

			// Log request completion
			log.info("<<< [{}] {} {} | status={} | duration={}ms",
					request.getMethod(),
					request.getRequestURI(),
					request.getQueryString() != null ? "?" + request.getQueryString() : "",
					response.getStatus(),
					duration);

			// Clean up MDC
			MDC.remove(LoggingConstants.MDC_REQUEST_ID);
			MDC.remove(LoggingConstants.MDC_CORRELATION_ID);
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		// Skip health checks and actuator endpoints
		return path.startsWith("/actuator") || path.equals("/health");
	}
}
