package com.gyp.eventservice.services.impl;

import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyp.eventservice.dtos.event.EventResponseDto;
import com.gyp.eventservice.dtos.seatmap.SeatAvailability;
import com.gyp.eventservice.services.EventCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventCacheServiceImpl implements EventCacheService {
	private static final Duration EVENT_TTL = Duration.ofMinutes(10);
	private static final Duration EVENT_LIST_TTL = Duration.ofMinutes(5);
	private static final Duration SEAT_AVAILABILITY_TTL = Duration.ofSeconds(60);
	private static final String EVENT_KEY_PREFIX = "event:details:";
	private static final String EVENT_ON_SALE_KEY = "event:list:on-sale";
	private static final String EVENT_COMING_KEY = "event:list:coming";
	private static final String EVENT_ACTIVE_KEY_PREFIX = "event:list:active:";
	private static final String SEAT_AVAILABILITY_KEY_PREFIX = "seat:availability:";

	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public EventResponseDto getEvent(String eventId) {
		Object value = redisTemplate.opsForValue().get(EVENT_KEY_PREFIX + eventId);
		if(value == null) {
			return null;
		}
		return objectMapper.convertValue(value, EventResponseDto.class);
	}

	@Override
	public void cacheEvent(EventResponseDto eventResponseDto) {
		if(eventResponseDto == null || eventResponseDto.getId() == null) {
			return;
		}
		redisTemplate.opsForValue().set(eventKey(eventResponseDto.getId()), eventResponseDto, EVENT_TTL);
	}

	@Override
	public void evictEvent(String eventId) {
		redisTemplate.delete(eventKey(eventId));
	}

	@Override
	public List<EventResponseDto> getEventsOnSale() {
		return getList(EVENT_ON_SALE_KEY, EventResponseDto.class);
	}

	@Override
	public void cacheEventsOnSale(List<EventResponseDto> events) {
		redisTemplate.opsForValue().set(EVENT_ON_SALE_KEY, events, EVENT_LIST_TTL);
	}

	@Override
	public void evictEventsOnSale() {
		redisTemplate.delete(EVENT_ON_SALE_KEY);
	}

	@Override
	public List<EventResponseDto> getComingEvents() {
		return getList(EVENT_COMING_KEY, EventResponseDto.class);
	}

	@Override
	public void cacheComingEvents(List<EventResponseDto> events) {
		redisTemplate.opsForValue().set(EVENT_COMING_KEY, events, EVENT_LIST_TTL);
	}

	@Override
	public void evictComingEvents() {
		redisTemplate.delete(EVENT_COMING_KEY);
	}

	@Override
	public List<EventResponseDto> getActiveEvents(String organizationId) {
		return getList(activeKey(organizationId), EventResponseDto.class);
	}

	@Override
	public void cacheActiveEvents(String organizationId, List<EventResponseDto> events) {
		if(organizationId == null || organizationId.isBlank()) {
			return;
		}
		redisTemplate.opsForValue().set(activeKey(organizationId), events, EVENT_LIST_TTL);
	}

	@Override
	public void evictActiveEvents(String organizationId) {
		if(organizationId == null || organizationId.isBlank()) {
			return;
		}
		redisTemplate.delete(activeKey(organizationId));
	}

	@Override
	public List<SeatAvailability> getSeatAvailability(String eventId) {
		return getList(seatAvailabilityKey(eventId), SeatAvailability.class);
	}

	@Override
	public void cacheSeatAvailability(String eventId, List<SeatAvailability> seats) {
		if(eventId == null || eventId.isBlank()) {
			return;
		}
		redisTemplate.opsForValue().set(seatAvailabilityKey(eventId), seats, SEAT_AVAILABILITY_TTL);
	}

	@Override
	public void evictSeatAvailability(String eventId) {
		if(eventId == null || eventId.isBlank()) {
			return;
		}
		redisTemplate.delete(seatAvailabilityKey(eventId));
	}

	@Override
	public void evictBookingLists(String organizationId) {
		evictEventsOnSale();
		evictComingEvents();
		evictActiveEvents(organizationId);
	}

	private String eventKey(String eventId) {
		return EVENT_KEY_PREFIX + eventId;
	}

	private String activeKey(String organizationId) {
		return EVENT_ACTIVE_KEY_PREFIX + organizationId;
	}

	private String seatAvailabilityKey(String eventId) {
		return SEAT_AVAILABILITY_KEY_PREFIX + eventId;
	}

	private <T> List<T> getList(String key, Class<T> elementClass) {
		Object value = redisTemplate.opsForValue().get(key);
		if(value == null) {
			return null;
		}
		return objectMapper.convertValue(
				value,
				objectMapper.getTypeFactory().constructCollectionType(List.class, elementClass)
		);
	}
}