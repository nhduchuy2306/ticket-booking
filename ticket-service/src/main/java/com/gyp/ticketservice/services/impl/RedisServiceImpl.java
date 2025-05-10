package com.gyp.ticketservice.services.impl;

import com.gyp.ticketservice.services.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public void saveData(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	@Override
	public Object getData(String key) {
		return redisTemplate.opsForValue().get(key);
	}
}
