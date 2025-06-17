package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.eventservice.dtos.eventpromotion.EventPromotionRequestDto;
import com.gyp.eventservice.dtos.eventpromotion.EventPromotionResponseDto;

public interface EventPromotionService {
	EventPromotionResponseDto createPromotion(String eventId, EventPromotionRequestDto request);

	EventPromotionResponseDto validatePromotionCode(String eventId, String code);

	List<EventPromotionResponseDto> getActivePromotions(String eventId);

	void deactivatePromotion(String promotionId);

	EventPromotionResponseDto calculateDiscount(String promotionCode, double originalPrice);
}
