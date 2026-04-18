package com.gyp.salechannelservice.services.impl;

import com.gyp.salechannelservice.dtos.salechannelevent.SaleChannelEventRequestDto;
import com.gyp.salechannelservice.entities.SaleChannelEventEntity;
import com.gyp.salechannelservice.mappers.SaleChannelEventMapper;
import com.gyp.salechannelservice.repositories.SaleChannelEventRepository;
import com.gyp.salechannelservice.repositories.SaleChannelRepository;
import com.gyp.salechannelservice.services.SaleChannelRedisCacheService;
import com.gyp.salechannelservice.services.SaleChannelEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleChannelEventServiceImpl implements SaleChannelEventService {
	private final SaleChannelEventRepository saleChannelEventRepository;
	private final SaleChannelRepository saleChannelRepository;
	private final SaleChannelEventMapper saleChannelEventMapper;
	private final SaleChannelRedisCacheService saleChannelRedisCacheService;

	@Override
	public void assignEventToChannel(String channelId, String eventId) {
		if(StringUtils.isEmpty(eventId)) {
			log.warn("Event ID is null or blank for channel ID: {}", channelId);
			return; // idempotent: do nothing
		}

		var saleChannel = saleChannelRepository.findById(channelId).orElse(null);
		if(saleChannel == null) {
			log.warn("Channel not found with ID: {}", channelId);
			return; // idempotent: do nothing
		}

		boolean alreadyExists = saleChannelEventRepository
				.findByEventIdAndSaleChannelEntity_Id(eventId, channelId)
				.isPresent();

		if(alreadyExists) {
			log.info("SaleChannelEvent already exists for event ID: {} and channel ID: {}", eventId, channelId);
			return; // idempotent: do nothing
		}

		SaleChannelEventRequestDto dto = SaleChannelEventRequestDto.builder()
				.saleChannelId(channelId)
				.eventId(eventId)
				.build();

		SaleChannelEventEntity entity = saleChannelEventMapper.toEntity(dto);
		saleChannelEventRepository.save(entity);
		saleChannelRedisCacheService.evictByPrefix("salechannel:event:" + eventId);
		log.info("Assigned event {} to channel {}", eventId, channelId);
	}

	@Override
	public void removeEventFromChannel(String channelId, String eventId) {
		if(StringUtils.isEmpty(eventId)) {
			log.warn("Event ID is null or blank for channel ID: {}", channelId);
			return; // idempotent: do nothing
		}

		var saleChannel = saleChannelRepository.findById(channelId).orElse(null);
		if(saleChannel == null) {
			log.warn("Channel not found with ID: {}", channelId);
			return; // idempotent: do nothing
		}

		var existingEvent = saleChannelEventRepository
				.findByEventIdAndSaleChannelEntity_Id(eventId, channelId);

		if(existingEvent.isEmpty()) {
			log.info("No SaleChannelEvent found for event ID: {} and channel ID: {}, nothing to remove.", eventId,
					channelId);
			return; // idempotent: do nothing
		}

		saleChannelEventRepository.delete(existingEvent.get());
		saleChannelRedisCacheService.evictByPrefix("salechannel:event:" + eventId);
		log.info("Removed event {} from channel {}", eventId, channelId);
	}
}
