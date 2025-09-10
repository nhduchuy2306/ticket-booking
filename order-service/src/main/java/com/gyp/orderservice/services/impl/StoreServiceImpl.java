package com.gyp.orderservice.services.impl;

import java.util.concurrent.TimeUnit;

import com.gyp.orderservice.services.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public Integer createCountDownSession(String sessionId) {
		int durationSeconds = 15 * 60; // 15 minutes
		redisTemplate.opsForValue().set(sessionId, "active", 15, TimeUnit.MINUTES);
		log.info("Created countdown session: {} with TTL of 15 minutes", sessionId);
		return durationSeconds;
	}

	@Override
	public Integer getCountDownSession(String sessionId) {
		Long expire = redisTemplate.getExpire(sessionId, TimeUnit.SECONDS);
		log.info("Checked countdown session: {} with remaining TTL: {} seconds", sessionId, expire);
		return expire > 0 ? expire.intValue() : null;
	}
}
