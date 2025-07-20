package com.gyp.ticketservice.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationResponseDto;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationSummaryDto;
import com.gyp.ticketservice.entities.TicketGenerationEntity;
import com.gyp.ticketservice.mappers.TicketGenerationMapper;
import com.gyp.ticketservice.repositories.TicketGenerationRepository;
import com.gyp.ticketservice.services.TicketGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketGenerationServiceImpl implements TicketGenerationService {
	private final TicketGenerationRepository ticketGenerationRepository;
	private final TicketGenerationMapper ticketGenerationMapper;

	@Override
	public TicketGenerationSummaryDto validateTicket(String ticketNumber) {
		Optional<TicketGenerationEntity> ticketGeneration = ticketGenerationRepository.findByTicketNumber(ticketNumber);
		if(ticketGeneration.isPresent()) {
			LocalDateTime eventDateTime = ticketGeneration.get().getEventDateTime();
			LocalDateTime current = LocalDateTime.now();
			if(eventDateTime.toLocalDate().isEqual(current.toLocalDate())) {
				return ticketGenerationMapper.toSummary(ticketGeneration.get());
			}
		}
		return null;
	}

	@Override
	public TicketGenerationResponseDto getTicketGenerationById(String id) {
		Optional<TicketGenerationEntity> ticketGeneration = ticketGenerationRepository.findById(id);
		return ticketGeneration.map(ticketGenerationMapper::toResponse).orElse(null);
	}

	@Override
	public void generateTicketBaseOnEventConfiguration(String eventId) {

	}
}
