package com.gyp.common.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Redis cache.
 *
 * <p>These properties can be customized via application.yml:</p>
 * <pre>
 * app:
 *   cache:
 *     ttl-minutes: 30
 *     key-prefix: "gyp:"
 *     enable-statistics: true
 * </pre>
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "app.cache")
public class CacheProperties {
	/**
	 * Default TTL in minutes for cache entries.
	 */
	private long ttlMinutes = 30;

	/**
	 * Key prefix for all cache entries.
	 */
	private String keyPrefix = "gyp:";

	/**
	 * Whether to enable cache statistics.
	 */
	private boolean enableStatistics = false;
}
