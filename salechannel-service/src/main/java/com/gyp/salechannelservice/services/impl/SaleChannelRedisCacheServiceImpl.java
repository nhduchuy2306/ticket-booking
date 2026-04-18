package com.gyp.salechannelservice.services.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyp.salechannelservice.services.SaleChannelRedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaleChannelRedisCacheServiceImpl implements SaleChannelRedisCacheService {
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public <T> T get(String key, Class<T> clazz) {
		if(key == null || key.isBlank()) {
			return null;
		}
		Object value = redisTemplate.opsForValue().get(key);
		if(value == null) {
			return null;
		}
		return objectMapper.convertValue(value, clazz);
	}

	@Override
	public <T> T get(String key, TypeReference<T> typeReference) {
		if(key == null || key.isBlank()) {
			return null;
		}

		Object value = redisTemplate.opsForValue().get(key);
		if(value == null) {
			return null;
		}
		return objectMapper.convertValue(value, typeReference);
	}

	@Override
	public void put(String key, Object value, Duration ttl) {
		if(key == null || key.isBlank() || value == null || ttl == null || ttl.isZero() || ttl.isNegative()) {
			return;
		}
		redisTemplate.opsForValue().set(key, value, ttl);
	}

	@Override
	public void evict(String key) {
		if(key == null || key.isBlank()) {
			return;
		}
		redisTemplate.delete(key);
	}

	@Override
	public void evictByPrefix(String prefix) {
		if(prefix == null || prefix.isBlank()) {
			return;
		}

		ScanOptions options = ScanOptions.scanOptions()
				.match(prefix + "*")
				.count(100)
				.build();

		List<String> keysToDelete = new ArrayList<>();

		try(Cursor<String> cursor = redisTemplate.scan(options)) {
			while(cursor.hasNext()) {
				keysToDelete.add(cursor.next());
			}
		} catch(Exception e) {
			throw new RuntimeException("Error scanning Redis keys", e);
		}

		if(!keysToDelete.isEmpty()) {
			redisTemplate.delete(keysToDelete);
		}
	}
}