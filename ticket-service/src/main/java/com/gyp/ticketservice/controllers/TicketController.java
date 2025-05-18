package com.gyp.ticketservice.controllers;

import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationSummaryDto;
import com.gyp.ticketservice.services.TicketGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(TicketController.TICKET_CONTROLLER_PATH)
public class TicketController extends AbstractController {
	public static final String TICKET_CONTROLLER_PATH = "/tickets";
	private static final String VALIDATE_TICKET_PATH = "validateticket";

	private static final String TICKET_NUMBER_PARAM = "ticketnumber";

	private final TicketGenerationService ticketGenerationService;

	@GetMapping("/" + VALIDATE_TICKET_PATH)
	public ResponseEntity<?> validateTicket(@RequestParam(TICKET_NUMBER_PARAM) String ticketNumber) {
		TicketGenerationSummaryDto ticketGenerationSummaryDto = ticketGenerationService.validateTicket(ticketNumber);
		if(ticketGenerationSummaryDto != null) {
			return ResponseEntity.ok().body(ticketGenerationSummaryDto);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
}
