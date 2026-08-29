package com.gyp.common.logging;

/**
 * Constants for logging MDC keys and headers.
 */
public final class LoggingConstants {
	private LoggingConstants() {
	}

	/**
	 * MDC key for the unique request ID.
	 */
	public static final String MDC_REQUEST_ID = "requestId";

	/**
	 * MDC key for correlation ID (for tracing across services).
	 */
	public static final String MDC_CORRELATION_ID = "correlationId";

	/**
	 * MDC key for the service name.
	 */
	public static final String MDC_SERVICE_NAME = "serviceName";

	/**
	 * HTTP header for request ID.
	 */
	public static final String HEADER_REQUEST_ID = "X-Request-ID";

	/**
	 * HTTP header for correlation ID.
	 */
	public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";
}
