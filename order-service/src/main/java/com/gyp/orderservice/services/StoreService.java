package com.gyp.orderservice.services;

public interface StoreService {
	Integer createCountDownSession(String sessionId);

	Integer getCountDownSession(String sessionId);
}
