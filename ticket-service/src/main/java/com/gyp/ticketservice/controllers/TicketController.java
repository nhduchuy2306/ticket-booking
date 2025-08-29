package com.gyp.ticketservice.controllers;

import java.util.Optional;

import com.gyp.common.controllers.AbstractController;
import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.enums.event.TicketStatus;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationSummaryDto;
import com.gyp.ticketservice.services.TicketGenerationService;
import com.gyp.ticketservice.services.TicketService;
import com.gyp.ticketservice.services.criteria.TicketSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(TicketController.TICKET_CONTROLLER_PATH)
public class TicketController extends AbstractController {
	public static final String TICKET_CONTROLLER_PATH = "/tickets";
	private static final String VALIDATE_TICKET_PATH = "validateticket";
	private static final String START_SALE_TICKET_PATH = "startsaleticket";
	private static final String AVAILABLE_TICKET_PATH = "availabletickets";

	private static final String TICKET_NUMBER_PARAM = "ticketnumber";
	private static final String EVENT_ID_PARAM = "eventId";

	private final TicketGenerationService ticketGenerationService;
	private final TicketService ticketService;

	@GetMapping("/" + VALIDATE_TICKET_PATH)
	public ResponseEntity<?> validateTicket(@RequestParam(TICKET_NUMBER_PARAM) String ticketNumber) {
		TicketGenerationSummaryDto ticketGenerationSummaryDto = ticketGenerationService.validateTicket(ticketNumber);
		if(ticketGenerationSummaryDto != null) {
			return ResponseEntity.ok().body(ticketGenerationSummaryDto);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@GetMapping
	public ResponseEntity<?> getAllTicketGeneration(
			@RequestParam(value = "eventId", required = false) String eventId,
			@RequestParam(value = "ticketId", required = false) String ticketId,
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size) {
		String organizationId = getCurrentOrganizationId();
		TicketSearchCriteria criteria = TicketSearchCriteria.builder()
				.eventId(eventId)
				.ticketId(ticketId)
				.organizationId(organizationId)
				.build();
		PaginatedDto pagination = PaginatedDto.builder()
				.page(page.orElse(0))
				.size(size.orElse(10))
				.build();
		var ticketGenerationResponse = ticketService.getAllTickets(criteria, pagination);
		return ResponseEntity.ok().body(ticketGenerationResponse);
	}

	@GetMapping(AVAILABLE_TICKET_PATH + "/{" + EVENT_ID_PARAM + "}")
	public ResponseEntity<?> getAvailableTicketsByEventId(@PathVariable(EVENT_ID_PARAM) String eventId) {
		String organizationId = getCurrentOrganizationId();
		TicketSearchCriteria criteria = TicketSearchCriteria.builder()
				.eventId(eventId)
				.status(TicketStatus.ON_SALE.name())
				.organizationId(organizationId)
				.build();
		var tickets = ticketService.getAllTickets(criteria, null);
		if(tickets != null) {
			return ResponseEntity.ok().body(tickets);
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping(START_SALE_TICKET_PATH + "/{" + EVENT_ID_PARAM + "}")
	public ResponseEntity<?> startSaleTicket(@PathVariable(EVENT_ID_PARAM) String eventId) {
		try {
			ticketService.startSaleTicket(eventId);
			return ResponseEntity.ok("Ticket sale started successfully for event ID: " + eventId);
		} catch(Exception e) {
			return ResponseEntity.status(500).body("Error starting ticket sale: " + e.getMessage());
		}
	}
}
