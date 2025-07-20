package com.gyp.eventservice.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.models.SeatMapEventModel;
import com.gyp.eventservice.dtos.seatmap.Position;
import com.gyp.eventservice.dtos.seatmap.Row;
import com.gyp.eventservice.dtos.seatmap.Seat;
import com.gyp.eventservice.dtos.seatmap.SeatAvailability;
import com.gyp.eventservice.dtos.seatmap.SeatConfig;
import com.gyp.eventservice.dtos.seatmap.SeatMapRequestDto;
import com.gyp.eventservice.dtos.seatmap.SeatMapResponseDto;
import com.gyp.eventservice.dtos.seatmap.SeatMapUtil;
import com.gyp.eventservice.dtos.seatmap.SeatStatus;
import com.gyp.eventservice.dtos.seatmap.SeatWithScore;
import com.gyp.eventservice.dtos.seatmap.Section;
import com.gyp.eventservice.dtos.seatmap.SectionType;
import com.gyp.eventservice.dtos.seatmap.StageConfig;
import com.gyp.eventservice.dtos.seatmap.Table;
import com.gyp.eventservice.dtos.seatmap.VenueMap;
import com.gyp.eventservice.mappers.SeatMapMapper;
import com.gyp.eventservice.repositories.SeatMapRepository;
import com.gyp.eventservice.repositories.VenueRepository;
import com.gyp.eventservice.services.SeatMapService;
import com.gyp.eventservice.services.SeatMapTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatMapServiceImpl implements SeatMapService {
	private final SeatMapTemplateService seatMapTemplateService;
	private final SeatMapRepository seatMapRepository;
	private final VenueRepository venueRepository;
	private final SeatMapMapper seatMapMapper;

	@Override
	public String convertOrganizerJson(String content) {
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
	public VenueMap createTheaterSeatMap(String name, int rows, int seatsPerRow) {
		return seatMapTemplateService.createTheaterTemplate(name, rows, seatsPerRow);
	}

	@Override
	public VenueMap createStadiumConcertSeatMap(String name, boolean includeField) {
		return seatMapTemplateService.createStadiumConcertTemplate(name, includeField);
	}

	@Override
	public VenueMap createConferenceSeatMap(String name, int numberOfTables, int seatsPerTable) {
		return seatMapTemplateService.createConferenceTemplate(name, numberOfTables, seatsPerTable);
	}

	@Override
	public VenueMap createArenaConcertSeatMap(String name) {
		return seatMapTemplateService.createArenaConcertTemplate(name);
	}

	@Override
	public VenueMap createClubSeatMap(String name, int standingCapacity) {
		return seatMapTemplateService.createClubTemplate(name, standingCapacity);
	}

	@Override
	public List<SeatWithScore> findBestSeats(String venueMapId, int limit) {
		VenueMap venueMap = findVenueMapById(venueMapId);

		List<SeatWithScore> allSeatsWithScore = new ArrayList<>();

		for(Section section : venueMap.getSeatConfig().getSections()) {
			if(Objects.equals(section.getType(), SectionType.SEATED)) {
				for(Row row : section.getRows()) {
					for(Seat seat : row.getSeats()) {
						if(Objects.equals(seat.getStatus(), SeatStatus.AVAILABLE)) {
							Position absolutePos = SeatMapUtil.calculateAbsoluteSeatPosition(
									venueMap, section.getId(), row.getId(), seat.getId()
							);
							double score = venueMap.getStageConfig().calculateSeatQualityScore(absolutePos);
							allSeatsWithScore.add(new SeatWithScore(seat, score));
						}
					}
				}
			}
		}

		return allSeatsWithScore.stream()
				.sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
				.limit(limit)
				.collect(Collectors.toList());
	}

	@Override
	public List<Seat> findSeatsBySection(String venueMapId, String sectionId, SeatStatus status) {
		VenueMap venueMap = findVenueMapById(venueMapId);

		return venueMap.getSeatConfig().getSections().stream()
				.filter(section -> section.getId().equals(sectionId))
				.flatMap(section -> {
					if(Objects.equals(section.getType(), SectionType.SEATED)) {
						return section.getRows().stream()
								.flatMap(row -> row.getSeats().stream());
					} else if(Objects.equals(section.getType(), SectionType.TABLE)) {
						return section.getTables().stream()
								.flatMap(table -> table.getSeats().stream());
					} else {
						return Stream.empty();
					}
				})
				.filter(seat -> status == null || Objects.equals(seat.getStatus(), status))
				.collect(Collectors.toList());
	}

	@Override
	public boolean reserveSeat(String venueMapId, String seatId) {
		VenueMap venueMap = findVenueMapById(venueMapId);

		Seat seat = findSeatById(venueMap, seatId);
		if(seat != null && Objects.equals(seat.getStatus(), SeatStatus.AVAILABLE)) {
			seat.setStatus(SeatStatus.RESERVED);
			return true;
		}
		return false;
	}

	@Override
	public boolean confirmSeatReservation(String venueMapId, String seatId) {
		VenueMap venueMap = findVenueMapById(venueMapId);

		Seat seat = findSeatById(venueMap, seatId);
		if(seat != null && Objects.equals(seat.getStatus(), SeatStatus.RESERVED)) {
			seat.setStatus(SeatStatus.SOLD);
			return true;
		}
		return false;
	}

	@Override
	public SeatAvailability checkSeatAvailability(String seatMapId, List<String> seatIds) {
		return null;
	}

	@Override
	public SeatConfig parseSeatConfig(String seatConfigJson) {
		return null;
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

	private Seat findSeatById(VenueMap venueMap, String seatId) {
		for(Section section : venueMap.getSeatConfig().getSections()) {
			if(Objects.equals(section.getType(), SectionType.SEATED)) {
				for(Row row : section.getRows()) {
					for(Seat seat : row.getSeats()) {
						if(seat.getId().equals(seatId)) {
							return seat;
						}
					}
				}
			} else if(Objects.equals(section.getType(), SectionType.TABLE)) {
				for(Table table : section.getTables()) {
					for(Seat seat : table.getSeats()) {
						if(seat.getId().equals(seatId)) {
							return seat;
						}
					}
				}
			}
		}
		return null;
	}

	private VenueMap findVenueMapById(String venueMapId) {
		return new VenueMap();
	}
}
