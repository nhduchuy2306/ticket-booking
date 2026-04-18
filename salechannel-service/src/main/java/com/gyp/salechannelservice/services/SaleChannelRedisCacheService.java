package com.gyp.salechannelservice.services;

import java.time.Duration;

import com.fasterxml.jackson.core.type.TypeReference;

public interface SaleChannelRedisCacheService {
	<T> T get(String key, Class<T> clazz);

	<T> T get(String key, TypeReference<T> typeReference);

	void put(String key, Object value, Duration ttl);

	void evict(String key);

	void evictByPrefix(String prefix);
}