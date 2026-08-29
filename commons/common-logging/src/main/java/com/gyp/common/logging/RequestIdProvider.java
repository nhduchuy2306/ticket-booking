package com.gyp.common.logging;

import org.slf4j.MDC;

/**
 * Utility class to access the current request's ID from MDC.
 *
 * <p>Usage example:</p>
 * <pre>
 * String currentRequestId = RequestIdProvider.getRequestId();
 * log.info("Processing with requestId: {}", currentRequestId);
 * </pre>
 */
public final class RequestIdProvider {
	private RequestIdProvider() {
	}

	/**
	 * Gets the current request ID from MDC.
	 *
	 * @return the current request ID, or "N/A" if not set
	 */
	public static String getRequestId() {
		String requestId = MDC.get(LoggingConstants.MDC_REQUEST_ID);
		return requestId != null ? requestId : "N/A";
	}

	/**
	 * Gets the current correlation ID from MDC.
	 *
	 * @return the current correlation ID, or "N/A" if not set
	 */
	public static String getCorrelationId() {
		String correlationId = MDC.get(LoggingConstants.MDC_CORRELATION_ID);
		return correlationId != null ? correlationId : "N/A";
	}
}
