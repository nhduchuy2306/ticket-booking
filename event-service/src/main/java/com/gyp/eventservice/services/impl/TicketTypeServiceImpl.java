package com.gyp.eventservice.services.impl;

import java.util.List;

import com.gyp.eventservice.dtos.seatmap.Seat;
import com.gyp.eventservice.dtos.tickettype.TicketTypeRequestDto;
import com.gyp.eventservice.dtos.tickettype.TicketTypeResponseDto;
import com.gyp.eventservice.entities.TicketTypeEntity;
import com.gyp.eventservice.mappers.TicketTypeMapper;
import com.gyp.eventservice.repositories.TicketTypeRepository;
import com.gyp.eventservice.services.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {
	private final TicketTypeRepository ticketTypeRepository;
	private final TicketTypeMapper ticketTypeMapper;

	@Override
	public double getEffectivePrice(Seat seat) {
		TicketTypeEntity ticketType = ticketTypeRepository.findById(seat.getTicketTypeId())
				.orElseThrow(() -> new RuntimeException("TicketType not found"));
		return ticketType.getPrice();
	}

	@Override
	public List<TicketTypeResponseDto> getTicketTypes() {
		List<TicketTypeEntity> ticketTypeEntities = ticketTypeRepository.findAll();
		if(ticketTypeEntities.isEmpty()) {
			throw new RuntimeException("No TicketTypes found");
		}
		return ticketTypeMapper.toResponseDtoList(ticketTypeEntities);
	}

	@Override
	public TicketTypeResponseDto getTicketTypeById(String ticketTypeId) {
		var ticketType = ticketTypeRepository.findById(ticketTypeId);
		if(ticketType.isEmpty()) {
			throw new RuntimeException("TicketType not found");
		}
		return ticketTypeMapper.toResponseDto(ticketType.get());
	}

	@Override
	public TicketTypeResponseDto getTicketTypeByName(String ticketTypeName) {
		var ticketType = ticketTypeRepository.findByName(ticketTypeName)
				.orElseThrow(() -> new RuntimeException("TicketType not found"));
		return ticketTypeMapper.toResponseDto(ticketType);
	}

	@Override
	public TicketTypeResponseDto createTicketType(TicketTypeRequestDto ticketTypeDto) {
		TicketTypeEntity ticketType = ticketTypeMapper.toEntity(ticketTypeDto);
		ticketType = ticketTypeRepository.save(ticketType);
		return ticketTypeMapper.toResponseDto(ticketType);
	}

	@Override
	public TicketTypeResponseDto updateTicketType(String ticketTypeId, TicketTypeRequestDto ticketTypeDto) {
		TicketTypeEntity existingTicketType = ticketTypeRepository.findById(ticketTypeId)
				.orElseThrow(() -> new RuntimeException("TicketType not found"));

		ticketTypeMapper.updateEntityFromDto(ticketTypeDto, existingTicketType);

		TicketTypeEntity updatedTicketType = ticketTypeRepository.save(existingTicketType);
		return ticketTypeMapper.toResponseDto(updatedTicketType);
	}

	@Override
	public void deleteTicketType(String ticketTypeId) {
		if(!ticketTypeRepository.existsById(ticketTypeId)) {
			throw new RuntimeException("TicketType not found");
		}
		ticketTypeRepository.deleteById(ticketTypeId);
	}
}
