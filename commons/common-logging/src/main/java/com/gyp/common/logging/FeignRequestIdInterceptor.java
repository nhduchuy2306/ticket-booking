package com.gyp.common.logging;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;

/**
 * Feign interceptor that propagates requestId and correlationId
 * to downstream service calls.
 *
 * <p>This ensures that the same requestId/correlationId is passed
 * through the entire call chain for distributed tracing.</p>
 */
public class FeignRequestIdInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		String requestId = MDC.get(LoggingConstants.MDC_REQUEST_ID);
		if(requestId != null) {
			template.header(LoggingConstants.HEADER_REQUEST_ID, requestId);
		}

		String correlationId = MDC.get(LoggingConstants.MDC_CORRELATION_ID);
		if(correlationId != null) {
			template.header(LoggingConstants.HEADER_CORRELATION_ID, correlationId);
		}
	}
}
