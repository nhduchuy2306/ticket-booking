package com.gyp.salechannelservice.services.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.gyp.common.enums.salechannel.SaleChannelStatus;
import com.gyp.common.enums.salechannel.SaleChannelType;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.salechannelservice.dtos.salechannel.SaleChannelRequestDto;
import com.gyp.salechannelservice.dtos.salechannel.SaleChannelResponseDto;
import com.gyp.salechannelservice.mappers.SaleChannelMapper;
import com.gyp.salechannelservice.repositories.SaleChannelRepository;
import com.gyp.salechannelservice.services.SaleChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleChannelServiceImpl implements SaleChannelService {
	private final SaleChannelRepository saleChannelRepository;
	private final SaleChannelMapper saleChannelMapper;

	@Override
	public SaleChannelResponseDto createSaleChannel(SaleChannelRequestDto saleChannel) {
		var entity = saleChannelMapper.toEntity(saleChannel);
		entity.setStatus(SaleChannelStatus.ACTIVE);
		var savedEntity = saleChannelRepository.save(entity);
		return saleChannelMapper.toResponseDto(savedEntity);
	}

	@Override
	public SaleChannelResponseDto updateSaleChannel(String id, SaleChannelRequestDto saleChannel) {
		var existingEntity = saleChannelRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sale channel not found with id: " + id));
		if(Objects.nonNull(existingEntity)) {
			saleChannelMapper.updateEntityFromDto(saleChannel, existingEntity);
			var updatedEntity = saleChannelRepository.save(existingEntity);
			return saleChannelMapper.toResponseDto(updatedEntity);
		} else {
			throw new ResourceNotFoundException("Sale channel not found with id: " + id);
		}
	}

	@Override
	public Optional<SaleChannelResponseDto> getSaleChannelById(String id) {
		return saleChannelRepository.findById(id)
				.map(saleChannelMapper::toResponseDto)
				.or(() -> {
					log.warn("Sale channel not found with id: {}", id);
					return Optional.empty();
				});
	}

	@Override
	public List<SaleChannelResponseDto> getAllSaleChannels() {
		List<SaleChannelResponseDto> responseList = saleChannelRepository.findAll()
				.stream()
				.map(saleChannelMapper::toResponseDto)
				.toList();
		if(responseList.isEmpty()) {
			log.warn("No sale channels found.");
		}
		return responseList;
	}

	@Override
	public List<SaleChannelResponseDto> getAllSaleChannelsByEventId(String eventId) {
		return saleChannelRepository.findAllByEventId(eventId)
				.stream()
				.map(saleChannelMapper::toResponseDto)
				.toList();
	}

	@Override
	public List<SaleChannelResponseDto> getActiveSaleChannels() {
		List<SaleChannelResponseDto> responseList = saleChannelRepository.findByStatus(SaleChannelStatus.ACTIVE)
				.stream()
				.map(saleChannelMapper::toResponseDto)
				.toList();
		if(responseList.isEmpty()) {
			log.warn("No active sale channels found.");
		}
		return responseList;
	}

	@Override
	public List<SaleChannelResponseDto> getSaleChannelsByType(SaleChannelType type) {
		if(type == null) {
			log.warn("Sale channel type is null.");
			return List.of();
		}
		List<SaleChannelResponseDto> responseList = saleChannelRepository.findByType(type)
				.stream()
				.map(saleChannelMapper::toResponseDto)
				.toList();
		if(responseList.isEmpty()) {
			log.warn("No sale channels found for type: {}", type);
		}
		return responseList;
	}

	@Override
	public void activateChannel(String id) {
		var saleChannel = saleChannelRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sale channel not found with id: " + id));
		saleChannel.setStatus(SaleChannelStatus.ACTIVE);
		saleChannelRepository.save(saleChannel);
	}

	@Override
	public void deactivateChannel(String id) {
		var saleChannel = saleChannelRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sale channel not found with id: " + id));
		saleChannel.setStatus(SaleChannelStatus.INACTIVE);
		saleChannelRepository.save(saleChannel);
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
	}
}
