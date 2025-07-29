package com.gyp.ticketservice.controllers;

import com.gyp.ticketservice.services.TicketGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(TicketGenerationController.TICKET_GENERATION_CONTROLLER_PATH)
public class TicketGenerationController {
	public static final String TICKET_GENERATION_CONTROLLER_PATH = "/ticketgenerations";

	private static final String GENERATE_TICKET_PATH = "generateticket";

	private static final String EVENT_ID_PARAM = "eventId";

	private final TicketGenerationService ticketGenerationService;

	@GetMapping("/{" + EVENT_ID_PARAM + "}/" + GENERATE_TICKET_PATH)
	public ResponseEntity<?> generateTicket(@PathVariable(EVENT_ID_PARAM) String eventId) {
		try {
			ticketGenerationService.generateTicketBaseOnEventConfiguration(eventId);
			return ResponseEntity.ok("Ticket generated successfully for event ID: " + eventId);
		} catch(Exception e) {
			return ResponseEntity.status(500).body("Error generating ticket: " + e.getMessage());
		}
	}
}
