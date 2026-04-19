package com.gyp.salechannelservice.services.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gyp.common.utils.SecurityUtils;
import com.gyp.common.enums.salechannel.SaleChannelStatus;
import com.gyp.common.enums.salechannel.SaleChannelType;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.salechannelservice.dtos.salechannel.SaleChannelRequestDto;
import com.gyp.salechannelservice.dtos.salechannel.SaleChannelResponseDto;
import com.gyp.salechannelservice.entities.SaleChannelEntity;
import com.gyp.salechannelservice.mappers.SaleChannelMapper;
import com.gyp.salechannelservice.repositories.SaleChannelRepository;
import com.gyp.salechannelservice.services.SaleChannelRedisCacheService;
import com.gyp.salechannelservice.services.SaleChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleChannelServiceImpl implements SaleChannelService {
	private static final Duration SALE_CHANNEL_TTL = Duration.ofMinutes(10);
	private static final Duration SALE_CHANNEL_LIST_TTL = Duration.ofMinutes(5);
	private static final String SALE_CHANNEL_LIST_KEY = "salechannel:list:all";
	private static final String SALE_CHANNEL_ACTIVE_KEY = "salechannel:list:active";
	private static final String SALE_CHANNEL_KEY_PREFIX = "salechannel:id:";
	private static final String SALE_CHANNEL_EVENT_KEY_PREFIX = "salechannel:event:";
	private static final String SALE_CHANNEL_TYPE_KEY_PREFIX = "salechannel:type:";

	private final SaleChannelRepository saleChannelRepository;
	private final SaleChannelMapper saleChannelMapper;
	private final SaleChannelRedisCacheService saleChannelRedisCacheService;

	@Override
	public SaleChannelResponseDto getSaleChannelBySlug(String orgSlug) {
		if(StringUtils.isEmpty(orgSlug)) {
			return null;
		}
		return saleChannelRepository.findFirstByOrganizationSlugAndType(orgSlug, SaleChannelType.TICKET_SHOP)
				.map(saleChannelMapper::toResponseDto)
				.orElse(null);
	}

	@Override
	public SaleChannelResponseDto updateCurrentOrganizationConfig(com.gyp.salechannelservice.dtos.salechannelconfig.SaleChannelConfig saleChannelConfig,
			String orgSlug) {
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		SaleChannelEntity saleChannelEntity = saleChannelRepository
				.findFirstByTypeAndOrganizationId(SaleChannelType.TICKET_SHOP, organizationId)
				.orElseGet(() -> {
					SaleChannelEntity entity = new SaleChannelEntity();
					entity.setOrganizationId(organizationId);
					entity.setOrganizationSlug(orgSlug);
					entity.setType(SaleChannelType.TICKET_SHOP);
					entity.setStatus(SaleChannelStatus.ACTIVE);
					entity.setName("Ticket Shop");
					return entity;
				});
		if(orgSlug != null && !orgSlug.isBlank()) {
			saleChannelEntity.setOrganizationSlug(orgSlug);
		}
		if(saleChannelEntity.getStartSaleAt() == null) {
			saleChannelEntity.setStartSaleAt(LocalDateTime.now());
		}
		if(saleChannelEntity.getEndSaleAt() == null) {
			saleChannelEntity.setEndSaleAt(LocalDateTime.now().plusYears(1));
		}
		saleChannelEntity.setSaleChannelConfig(saleChannelMapper.mapSaleChannelConfigToJson(saleChannelConfig));
		saleChannelEntity = saleChannelRepository.save(saleChannelEntity);
		evictSaleChannelCaches(saleChannelEntity.getId(), saleChannelEntity.getType());
		return saleChannelMapper.toResponseDto(saleChannelEntity);
	}

	@Override
	public SaleChannelResponseDto createSaleChannel(SaleChannelRequestDto saleChannel) {
		var entity = saleChannelMapper.toEntity(saleChannel);
		entity.setStatus(SaleChannelStatus.ACTIVE);
		if(entity.getStartSaleAt() == null) {
			entity.setStartSaleAt(LocalDateTime.now());
		}
		if(entity.getEndSaleAt() == null) {
			entity.setEndSaleAt(LocalDateTime.now().plusYears(1));
		}
		var savedEntity = saleChannelRepository.save(entity);
		SaleChannelResponseDto responseDto = saleChannelMapper.toResponseDto(savedEntity);
		evictSaleChannelCaches(savedEntity.getId(), savedEntity.getType());
		return responseDto;
	}

	@Override
	public SaleChannelResponseDto updateSaleChannel(String id, SaleChannelRequestDto saleChannel) {
		var existingEntity = saleChannelRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sale channel not found with id: " + id));
		if(Objects.nonNull(existingEntity)) {
			SaleChannelType previousType = existingEntity.getType();
			saleChannelMapper.updateEntityFromDto(saleChannel, existingEntity);
			var updatedEntity = saleChannelRepository.save(existingEntity);
			SaleChannelResponseDto responseDto = saleChannelMapper.toResponseDto(updatedEntity);
			evictSaleChannelCaches(updatedEntity.getId(), previousType);
			evictSaleChannelTypeCaches(updatedEntity.getType());
			return responseDto;
		} else {
			throw new ResourceNotFoundException("Sale channel not found with id: " + id);
		}
	}

	@Override
	public Optional<SaleChannelResponseDto> getSaleChannelById(String id) {
		SaleChannelResponseDto cachedSaleChannel = saleChannelRedisCacheService.get(saleChannelKey(id),
				SaleChannelResponseDto.class);
		if(cachedSaleChannel != null) {
			return Optional.of(cachedSaleChannel);
		}
		return saleChannelRepository.findById(id)
				.map(entity -> {
					SaleChannelResponseDto responseDto = saleChannelMapper.toResponseDto(entity);
					saleChannelRedisCacheService.put(saleChannelKey(id), responseDto, SALE_CHANNEL_TTL);
					return responseDto;
				})
				.or(() -> {
					log.warn("Sale channel not found with id: {}", id);
					return Optional.empty();
				});
	}

	@Override
	public List<SaleChannelResponseDto> getAllSaleChannels() {
		List<SaleChannelResponseDto> cachedSaleChannels = saleChannelRedisCacheService.get(SALE_CHANNEL_LIST_KEY,
				new TypeReference<>() {});
		if(cachedSaleChannels != null) {
			return cachedSaleChannels;
		}
		List<SaleChannelResponseDto> responseList = saleChannelRepository.findAll()
				.stream()
				.map(saleChannelMapper::toResponseDto)
				.toList();
		if(responseList.isEmpty()) {
			log.warn("No sale channels found.");
		}
		saleChannelRedisCacheService.put(SALE_CHANNEL_LIST_KEY, responseList, SALE_CHANNEL_LIST_TTL);
		return responseList;
	}

	@Override
	public List<SaleChannelResponseDto> getAllSaleChannelsByEventId(String eventId) {
		List<SaleChannelResponseDto> cachedSaleChannels = saleChannelRedisCacheService.get(
				saleChannelEventKey(eventId), new TypeReference<>() {});
		if(cachedSaleChannels != null) {
			return cachedSaleChannels;
		}
		List<SaleChannelResponseDto> responseList = saleChannelRepository.findAllByEventId(eventId)
				.stream()
				.map(saleChannelMapper::toResponseDto)
				.toList();
		saleChannelRedisCacheService.put(saleChannelEventKey(eventId), responseList, SALE_CHANNEL_LIST_TTL);
		return responseList;
	}

	@Override
	public List<SaleChannelResponseDto> getActiveSaleChannels() {
		List<SaleChannelResponseDto> cachedSaleChannels = saleChannelRedisCacheService.get(SALE_CHANNEL_ACTIVE_KEY,
				new TypeReference<>() {});
		if(cachedSaleChannels != null) {
			return cachedSaleChannels;
		}
		List<SaleChannelResponseDto> responseList = saleChannelRepository.findByStatus(SaleChannelStatus.ACTIVE)
				.stream()
				.map(saleChannelMapper::toResponseDto)
				.toList();
		if(responseList.isEmpty()) {
			log.warn("No active sale channels found.");
		}
		saleChannelRedisCacheService.put(SALE_CHANNEL_ACTIVE_KEY, responseList, SALE_CHANNEL_LIST_TTL);
		return responseList;
	}

	@Override
	public List<SaleChannelResponseDto> getSaleChannelsByType(SaleChannelType type) {
		if(type == null) {
			log.warn("Sale channel type is null.");
			return List.of();
		}
		List<SaleChannelResponseDto> cachedSaleChannels = saleChannelRedisCacheService.get(saleChannelTypeKey(type),
				new TypeReference<>() {});
		if(cachedSaleChannels != null) {
			return cachedSaleChannels;
		}
		List<SaleChannelResponseDto> responseList = saleChannelRepository.findByType(type)
				.stream()
				.map(saleChannelMapper::toResponseDto)
				.toList();
		if(responseList.isEmpty()) {
			log.warn("No sale channels found for type: {}", type);
		}
		saleChannelRedisCacheService.put(saleChannelTypeKey(type), responseList, SALE_CHANNEL_LIST_TTL);
		return responseList;
	}

	@Override
	public void activateChannel(String id) {
		var saleChannel = saleChannelRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sale channel not found with id: " + id));
		saleChannel.setStatus(SaleChannelStatus.ACTIVE);
		saleChannelRepository.save(saleChannel);
		evictSaleChannelCaches(id, saleChannel.getType());
	}

	@Override
	public void deactivateChannel(String id) {
		var saleChannel = saleChannelRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sale channel not found with id: " + id));
		saleChannel.setStatus(SaleChannelStatus.INACTIVE);
		saleChannelRepository.save(saleChannel);
		evictSaleChannelCaches(id, saleChannel.getType());
	}

	@Override
	public void deleteSaleChannel(String id) {
		if(StringUtils.isEmpty(id)) {
			log.warn("Sale channel ID is null or blank.");
			throw new IllegalArgumentException("Sale channel ID cannot be null or blank.");
		}
		var saleChannel = saleChannelRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sale channel not found with id: " + id));
		saleChannelRepository.delete(saleChannel);
		evictSaleChannelCaches(id, saleChannel.getType());
	}

	private String saleChannelKey(String id) {
		return SALE_CHANNEL_KEY_PREFIX + id;
	}

	private String saleChannelEventKey(String eventId) {
		return SALE_CHANNEL_EVENT_KEY_PREFIX + eventId;
	}

	private String saleChannelTypeKey(SaleChannelType type) {
		return SALE_CHANNEL_TYPE_KEY_PREFIX + type.name();
	}

	private void evictSaleChannelTypeCaches(SaleChannelType type) {
		if(type != null) {
			saleChannelRedisCacheService.evict(saleChannelTypeKey(type));
		}
	}

	private void evictSaleChannelCaches(String saleChannelId, SaleChannelType type) {
		saleChannelRedisCacheService.evict(saleChannelKey(saleChannelId));
		saleChannelRedisCacheService.evict(SALE_CHANNEL_LIST_KEY);
		saleChannelRedisCacheService.evict(SALE_CHANNEL_ACTIVE_KEY);
		saleChannelRedisCacheService.evictByPrefix(SALE_CHANNEL_EVENT_KEY_PREFIX);
		evictSaleChannelTypeCaches(type);
	}
}
