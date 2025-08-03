package com.gyp.eventservice.services.impl;

import java.util.List;

import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.enums.event.SeasonStatus;
import com.gyp.common.exceptions.ResourceDuplicateException;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.services.ValidationService;
import com.gyp.common.utils.PropertyName;
import com.gyp.common.utils.SecurityUtils;
import com.gyp.common.validators.criteria.ValidationInfo;
import com.gyp.common.validators.rulecheck.CheckFactory;
import com.gyp.eventservice.dtos.season.SeasonRequestDto;
import com.gyp.eventservice.dtos.season.SeasonResponseDto;
import com.gyp.eventservice.entities.SeasonEntity;
import com.gyp.eventservice.mappers.SeasonMapper;
import com.gyp.eventservice.repositories.SeasonRepository;
import com.gyp.eventservice.services.SeasonService;
import com.gyp.eventservice.services.criteria.SeasonSearchCriteria;
import com.gyp.eventservice.services.specifications.SeasonSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
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
	public List<SeasonResponseDto> getAllSeasons(SeasonSearchCriteria criteria, PaginatedDto pagination) {
		Specification<SeasonEntity> seasonSpecification = SeasonSpecification.createSearchSeasonSpecification(criteria);
		Page<SeasonEntity> entities = seasonRepository.findAll(seasonSpecification, pagination.toPageable());
		if(!entities.isEmpty()) {
			return seasonMapper.toResponseDtoList(entities.getContent());
		}
		return null;
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
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		var seasonEntity = seasonMapper.toEntity(seasonRequestDto);
		seasonEntity.setStatus(SeasonStatus.ACTIVE);
		seasonEntity.setOrganizationId(organizationId);
		var savedSeasonEntity = seasonRepository.save(seasonEntity);
		return seasonMapper.toResponseDto(savedSeasonEntity);
	}

	@Override
	public SeasonResponseDto updateSeason(String seasonId, SeasonRequestDto seasonRequestDto)
			throws ResourceNotFoundException {
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		var existingSeason = seasonRepository.findById(seasonId)
				.orElseThrow(
						() -> new ResourceNotFoundException(String.format("Season with ID %s not found", seasonId)));
		if(existingSeason == null) {
			throw new ResourceNotFoundException("Season not found");
		}
		seasonMapper.updateEntityFromDto(seasonRequestDto, existingSeason);
		existingSeason.setOrganizationId(organizationId);
		var updatedSeasonEntity = seasonRepository.save(existingSeason);
		return seasonMapper.toResponseDto(updatedSeasonEntity);
	}

	@Override
	public void deleteSeason(String seasonId) throws ResourceNotFoundException {
		var existingSeason = seasonRepository.findById(seasonId)
				.orElseThrow(
						() -> new ResourceNotFoundException(String.format("Season with ID %s not found", seasonId)));
		if(existingSeason == null) {
			throw new ResourceNotFoundException("Season not found");
		}
		seasonRepository.delete(existingSeason);
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
