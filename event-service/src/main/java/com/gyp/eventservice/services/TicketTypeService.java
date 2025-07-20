package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.common.models.TicketTypeEventModel;
import com.gyp.eventservice.dtos.seatmap.Seat;
import com.gyp.eventservice.dtos.tickettype.TicketTypeRequestDto;
import com.gyp.eventservice.dtos.tickettype.TicketTypeResponseDto;

public interface TicketTypeService {

	double getEffectivePrice(Seat seat);

	List<TicketTypeResponseDto> getTicketTypes();

	TicketTypeResponseDto getTicketTypeById(String ticketTypeId);

	TicketTypeResponseDto getTicketTypeByName(String ticketTypeName);

	TicketTypeResponseDto createTicketType(TicketTypeRequestDto ticketTypeDto);

	TicketTypeResponseDto updateTicketType(String ticketTypeId, TicketTypeRequestDto ticketTypeDto);

	void deleteTicketType(String ticketTypeId);

	TicketTypeResponseDto toggleTicketTypeStatus(String ticketTypeId);

	List<TicketTypeEventModel> getListTicketTypeModel();
}
