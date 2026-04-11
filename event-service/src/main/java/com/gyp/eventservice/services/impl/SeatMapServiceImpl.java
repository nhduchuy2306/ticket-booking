package com.gyp.eventservice.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyp.common.converters.Serialization;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.models.SeatMapEventModel;
import com.gyp.eventservice.dtos.seatmap.SeatConfig;
import com.gyp.eventservice.dtos.seatmap.SeatMapRequestDto;
import com.gyp.eventservice.dtos.seatmap.SeatMapResponseDto;
import com.gyp.eventservice.dtos.seatmap.StageConfig;
import com.gyp.eventservice.mappers.SeatMapMapper;
import com.gyp.eventservice.repositories.SeatMapRepository;
import com.gyp.eventservice.services.SeatInventoryService;
import com.gyp.eventservice.services.SeatMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatMapServiceImpl implements SeatMapService {
	private final SeatMapRepository seatMapRepository;
	private final SeatMapMapper seatMapMapper;
	private final SeatInventoryService seatInventoryService;

	@Override
	public String convertSeatMapJson(String content) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode root = objectMapper.readTree(content);

			// 1. Extract and map `stage` part
			JsonNode stageNode = root.get("stageConfig");
			StageConfig stageConfig = objectMapper.treeToValue(stageNode, StageConfig.class);

			// 2. Extract and map `seatMap` part (polymorphic deserialization)
			JsonNode seatMapNode = root.get("seatConfig");
			SeatConfig seatConfig = objectMapper.treeToValue(seatMapNode, SeatConfig.class);

			// 3. (Optional) Return a combined DTO, string, or store/save
			Map<String, Object> result = new HashMap<>();
			result.put("stageConfig", stageConfig);
			result.put("seatConfig", seatConfig);
			return objectMapper.writeValueAsString(result);
		} catch(JsonProcessingException e) {
			throw new RuntimeException("Failed to parse seat map JSON", e);
		}
	}

	@Override
	public SeatConfig parseSeatConfig(String seatConfigJson) {
		try {
			return Serialization.deserializeFromString(seatConfigJson, SeatConfig.class);
		} catch(JsonProcessingException e) {
			throw new RuntimeException("Failed to parse seat config JSON", e);
		}
	}

	@Override
	public List<SeatMapResponseDto> getAllSeatMaps() {
		var seatMaps = seatMapRepository.findAll();
		if(seatMaps.isEmpty()) {
			return new ArrayList<>();
		}
		return seatMapMapper.toResponseDtoList(seatMaps);
	}

	@Override
	public SeatMapResponseDto getSeatMapById(String seatMapId) {
		var seatMapEntity = seatMapRepository.findById(seatMapId)
				.orElseThrow(() -> new ResourceNotFoundException("Seat Map Not Found"));
		if(seatMapEntity != null) {
			return seatMapMapper.toResponseDto(seatMapEntity);
		}
		return null;
	}

	@Override
	public SeatMapResponseDto updateSeatMap(String seatMapId, SeatMapRequestDto seatMapDto) {
		var seatMapEntity = seatMapRepository.findById(seatMapId)
				.orElseThrow(() -> new ResourceNotFoundException("Seat Map Not Found"));
		if(seatMapEntity != null) {
			seatMapMapper.updateEntityFromDto(seatMapDto, seatMapEntity);
			seatMapRepository.save(seatMapEntity);
			return seatMapMapper.toResponseDto(seatMapEntity);
		}
		return null;
	}

	@Override
	public SeatMapResponseDto createSeatMap(SeatMapRequestDto seatMapDto) {
		var seatMapEntity = seatMapMapper.toEntity(seatMapDto);
		seatMapRepository.save(seatMapEntity);
		return seatMapMapper.toResponseDto(seatMapEntity);
	}

	@Override
	public void deleteSeatMap(String seatMapId) {
		if(!seatMapRepository.existsById(seatMapId)) {
			throw new ResourceNotFoundException("Seat Map Not Found");
		}
		seatMapRepository.deleteById(seatMapId);
	}

	@Override
	public List<SeatMapEventModel> getListSeatMapModel() {
		var seatMaps = seatMapRepository.findAll();
		if(seatMaps.isEmpty()) {
			return new ArrayList<>();
		}
		return seatMaps.stream()
				.map(seatMapMapper::toSeatMapEventModel)
				.collect(Collectors.toList());
	}

	@Override
	public void generateSeatMapTicket(String eventId) {
		seatInventoryService.initializeSeatsForEvent(eventId);
	}
}
