package com.gyp.eventservice.services.impl;

import java.util.List;

import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.models.TicketTypeEventModel;
import com.gyp.common.utils.SecurityUtils;
import com.gyp.eventservice.dtos.seatmap.Seat;
import com.gyp.eventservice.dtos.tickettype.TicketTypeRequestDto;
import com.gyp.eventservice.dtos.tickettype.TicketTypeResponseDto;
import com.gyp.eventservice.entities.TicketTypeEntity;
import com.gyp.eventservice.mappers.TicketTypeMapper;
import com.gyp.eventservice.repositories.TicketTypeRepository;
import com.gyp.eventservice.services.TicketTypeService;
import com.gyp.eventservice.services.criteria.TicketTypeSearchCriteria;
import com.gyp.eventservice.services.specifications.TicketTypeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
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
	public List<TicketTypeResponseDto> getTicketTypes(TicketTypeSearchCriteria criteria, PaginatedDto pagination) {
		Specification<TicketTypeEntity> ticketTypeSpecification =
				TicketTypeSpecification.createSearchTicketTypeSpecification(criteria);
		Page<TicketTypeEntity> entities = ticketTypeRepository.findAll(ticketTypeSpecification,
				pagination.toPageable());
		if(!entities.isEmpty()) {
			return ticketTypeMapper.toResponseDtoList(entities.getContent());
		}
		return null;
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
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		TicketTypeEntity ticketType = ticketTypeMapper.toEntity(ticketTypeDto);
		ticketType.setOrganizationId(organizationId);
		ticketType = ticketTypeRepository.save(ticketType);
		return ticketTypeMapper.toResponseDto(ticketType);
	}

	@Override
	public TicketTypeResponseDto updateTicketType(String ticketTypeId, TicketTypeRequestDto ticketTypeDto) {
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		TicketTypeEntity existingTicketType = ticketTypeRepository.findById(ticketTypeId)
				.orElseThrow(() -> new RuntimeException("TicketType not found"));

		ticketTypeMapper.updateEntityFromDto(ticketTypeDto, existingTicketType);
		existingTicketType.setOrganizationId(organizationId);

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

	@Override
	public TicketTypeResponseDto toggleTicketTypeStatus(String ticketTypeId) {
		return null;
	}

	@Override
	public List<TicketTypeEventModel> getListTicketTypeModel() {
		var ticketTypes = ticketTypeRepository.findAll();
		if(!ticketTypes.isEmpty()) {
			return ticketTypeMapper.toEventModelList(ticketTypes);
		}
		return null;
	}

	@Override
	public List<TicketTypeResponseDto> getTicketTypesByIds(List<String> ids) {
		var ticketTypes = ticketTypeRepository.findAllByIdIn(ids);
		if(!ticketTypes.isEmpty()) {
			return ticketTypeMapper.toResponseDtoList(ticketTypes);
		}
		return null;
	}
}
