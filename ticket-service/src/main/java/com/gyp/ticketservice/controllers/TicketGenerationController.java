package com.gyp.ticketservice.controllers;

import com.gyp.common.controllers.AbstractController;
import com.gyp.ticketservice.services.TicketGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(TicketGenerationController.TICKET_GENERATION_CONTROLLER_PATH)
public class TicketGenerationController extends AbstractController {
	public static final String TICKET_GENERATION_CONTROLLER_PATH = "/ticket-generations";

	private static final String GENERATE_TICKET_PATH = "generate-ticket";

	private static final String EVENT_ID_PARAM = "eventId";

	private final TicketGenerationService ticketGenerationService;

	@DeleteMapping("/{" + EVENT_ID_PARAM + "}/" + GENERATE_TICKET_PATH)
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.TICKET_GENERATION, #ActionPerm.DELETE)")
	public ResponseEntity<?> deleteTicketsGeneration(@PathVariable(EVENT_ID_PARAM) String eventId) {
		try {
			ticketGenerationService.deleteTicketsGeneration(eventId);
			return ResponseEntity.ok("Ticket generated successfully for event ID: " + eventId);
		} catch(Exception e) {
			return ResponseEntity.status(500).body("Error generating ticket: " + e.getMessage());
		}
	}
}
