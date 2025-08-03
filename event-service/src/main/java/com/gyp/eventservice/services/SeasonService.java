package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.intefaces.Validatable;
import com.gyp.eventservice.dtos.season.SeasonRequestDto;
import com.gyp.eventservice.dtos.season.SeasonResponseDto;
import com.gyp.eventservice.services.criteria.SeasonSearchCriteria;

public interface SeasonService extends Validatable {
	List<SeasonResponseDto> getAllSeasons();

	List<SeasonResponseDto> getAllSeasons(SeasonSearchCriteria criteria, PaginatedDto pagination);

	SeasonResponseDto getSeasonById(String seasonId) throws ResourceNotFoundException;

	SeasonResponseDto createSeason(SeasonRequestDto seasonRequestDto);

	SeasonResponseDto updateSeason(String seasonId, SeasonRequestDto seasonRequestDto)
			throws ResourceNotFoundException;

	void deleteSeason(String seasonId) throws ResourceNotFoundException;

	SeasonResponseDto getActiveSeason();
}
