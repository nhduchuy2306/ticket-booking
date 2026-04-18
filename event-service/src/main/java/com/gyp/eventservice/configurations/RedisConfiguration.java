package com.gyp.eventservice.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory,
			ObjectMapper objectMapper) {
		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(serializer);

		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

		template.afterPropertiesSet();
		return template;
	}
}