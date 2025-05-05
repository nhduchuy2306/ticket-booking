package com.gyp.ticket.eventservice.services.impl;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyp.ticket.eventservice.dtos.seatmap.SeatConfig;
import com.gyp.ticket.eventservice.dtos.seatmap.StageConfig;
import com.gyp.ticket.eventservice.repositories.SeatMapRepository;
import com.gyp.ticket.eventservice.services.SeatMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatMapServiceImpl implements SeatMapService {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final SeatMapRepository seatMapRepository;

	@Override
	public String convertOrganizerJson(String content) {
		try {
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
}
