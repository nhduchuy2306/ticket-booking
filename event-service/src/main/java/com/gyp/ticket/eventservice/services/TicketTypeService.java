package com.gyp.ticket.eventservice.services;

import java.math.BigDecimal;

import com.gyp.ticket.eventservice.dtos.seatmap.Seat;

public interface TicketTypeService {

	double getEffectivePrice(Seat seat);
}
