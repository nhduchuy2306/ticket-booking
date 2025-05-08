package com.gyp.eventservice.services;

import com.gyp.eventservice.dtos.seatmap.Seat;

public interface TicketTypeService {

	double getEffectivePrice(Seat seat);
}
