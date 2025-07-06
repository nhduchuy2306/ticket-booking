package com.gyp.eventservice.services.impl;

import java.util.List;

import com.gyp.common.enums.event.SeasonStatus;
import com.gyp.common.exceptions.ResourceDuplicateException;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.services.ValidationService;
import com.gyp.common.utils.PropertyName;
import com.gyp.common.validators.criteria.ValidationInfo;
import com.gyp.common.validators.rulecheck.CheckFactory;
import com.gyp.eventservice.dtos.season.SeasonRequestDto;
import com.gyp.eventservice.dtos.season.SeasonResponseDto;
import com.gyp.eventservice.entities.SeasonEntity;
import com.gyp.eventservice.mappers.SeasonMapper;
import com.gyp.eventservice.repositories.SeasonRepository;
import com.gyp.eventservice.services.SeasonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeasonServiceImpl implements SeasonService {
	private final SeasonRepository seasonRepository;
	private final ValidationService validationService;
	private final SeasonMapper seasonMapper;
	private final CheckFactory checkFactory;

	@Override
	public List<SeasonResponseDto> getAllSeasons() {
		var seasons = seasonRepository.findAll();
		return seasonMapper.toResponseDtoList(seasons);
	}

	@Override
	public SeasonResponseDto getSeasonById(String seasonId) throws ResourceNotFoundException {
		var seasonEntity = seasonRepository.findById(seasonId).orElseThrow(
				() -> new ResourceNotFoundException(String.format("Season with ID %s not found", seasonId))
		);
		return seasonMapper.toResponseDto(seasonEntity);
	}

	@Override
	public SeasonResponseDto createSeason(SeasonRequestDto seasonRequestDto) {
		checkDuplicate(seasonRequestDto);
		var seasonEntity = seasonMapper.toEntity(seasonRequestDto);
		var savedSeasonEntity = seasonRepository.save(seasonEntity);
		return seasonMapper.toResponseDto(savedSeasonEntity);
	}

	@Override
	public SeasonResponseDto updateSeason(String seasonId, SeasonRequestDto seasonRequestDto)
			throws ResourceNotFoundException {
		var existingSeason = seasonRepository.findById(seasonId)
				.orElseThrow(
						() -> new ResourceNotFoundException(String.format("Season with ID %s not found", seasonId)));
		checkDuplicate(seasonRequestDto);
		seasonMapper.updateEntityFromDto(seasonRequestDto, existingSeason);
		var updatedSeasonEntity = seasonRepository.save(existingSeason);
		return seasonMapper.toResponseDto(updatedSeasonEntity);
	}

	@Override
	public void deleteSeason(String seasonId) throws ResourceNotFoundException {
		var existingSeason = seasonRepository.findById(seasonId)
				.orElseThrow(
						() -> new ResourceNotFoundException(String.format("Season with ID %s not found", seasonId)));
		seasonRepository.delete(existingSeason);
		log.info("Season with ID {} deleted successfully", seasonId);
	}

	@Override
	public SeasonResponseDto getActiveSeason() {
		var activeSeason = seasonRepository.findByStatus(SeasonStatus.ACTIVE)
				.orElseThrow(() -> new ResourceNotFoundException("No active season found"));
		return seasonMapper.toResponseDto(activeSeason);
	}

	private void checkDuplicate(SeasonRequestDto seasonRequestDto) {
		var checkDuplicate = checkFactory.createSingleFieldDuplicateCheck(SeasonEntity.class,
				PropertyName.of(seasonRequestDto::getName),
				seasonRequestDto.getName(), null);
		if(!checkDuplicate.isValid()) {
			log.error("Duplicate season name found: {}", seasonRequestDto.getName());
			throw new ResourceDuplicateException(checkDuplicate.getErrorMessage());
		}
	}

	@Override
	public ValidationInfo validate(Class<?> clazz) {
		var validationInfo = validationService.extractValidationInfo(clazz);
		if(validationInfo == null) {
			log.warn("No validation info found for request class: {}", clazz.getName());
			return ValidationInfo.empty();
		}
		return validationInfo;
	}
}
