package com.gyp.ticketservice.services;

public interface RedisService {
	void saveData(String key, Object value);
	Object getData(String key);
}
