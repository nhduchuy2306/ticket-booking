package com.gyp.common.cache;

import java.time.Duration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Auto-configuration for Redis Cache.
 *
 * <p>This configuration is automatically applied when the common-cache
 * module is on the classpath. No additional setup is required.</p>
 */
@Slf4j
@AutoConfiguration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
@RequiredArgsConstructor
@ConditionalOnClass({RedisConnectionFactory.class, CacheManager.class})
public class CacheAutoConfiguration {

	private final CacheProperties cacheProperties;

	@Bean
	public RedisCacheConfiguration redisCacheConfiguration() {
		return RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofMinutes(cacheProperties.getTtlMinutes()))
				.prefixCacheNameWith(cacheProperties.getKeyPrefix())
				.serializeKeysWith(
						RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(
						RedisSerializationContext.SerializationPair.fromSerializer(
								new GenericJackson2JsonRedisSerializer()))
				.disableCachingNullValues();
	}

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory,
			RedisCacheConfiguration redisCacheConfiguration) {
		log.info("Initializing Redis CacheManager with TTL={}min, prefix='{}'",
				cacheProperties.getTtlMinutes(), cacheProperties.getKeyPrefix());

		var builder = RedisCacheManager.builder(connectionFactory)
				.cacheDefaults(redisCacheConfiguration);

		if(cacheProperties.isEnableStatistics()) {
			builder.enableStatistics();
		}

		return builder.build();
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}
}
