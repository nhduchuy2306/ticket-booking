package com.gyp.ticketservice.controllers;

import java.util.Optional;

import com.gyp.common.controllers.AbstractController;
import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.seatmapservice.grpc.seatmap.SeatMapRequest;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationSummaryDto;
import com.gyp.ticketservice.messages.grpcs.SeatMapServiceGrpcClient;
import com.gyp.ticketservice.services.TicketGenerationService;
import com.gyp.ticketservice.services.TicketService;
import com.gyp.ticketservice.services.criteria.TicketSearchCriteria;
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
	private static final String EVENT_ID_PARAM = "eventId";
	private static final String USER_ID_PARAM = "userId";
	private static final String TICKET_ID_PARAM = "ticketId";

	private final TicketGenerationService ticketGenerationService;
	private final TicketService ticketService;
	private final SeatMapServiceGrpcClient seatMapServiceGrpcClient;

	@GetMapping("/" + VALIDATE_TICKET_PATH)
	public ResponseEntity<?> validateTicket(@RequestParam(TICKET_NUMBER_PARAM) String ticketNumber) {
		TicketGenerationSummaryDto ticketGenerationSummaryDto = ticketGenerationService.validateTicket(ticketNumber);
		if(ticketGenerationSummaryDto != null) {
			return ResponseEntity.ok().body(ticketGenerationSummaryDto);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@GetMapping("/seatmap")
	public ResponseEntity<?> getSeatMap(@RequestParam("eventId") String eventId) {
		var seatMap = seatMapServiceGrpcClient.getSeatMap(
				SeatMapRequest.newBuilder().setEventId(eventId).build());
		if(seatMap != null) {
			return ResponseEntity.ok().body(seatMap);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping
	public ResponseEntity<?> getTicketGenerationById(
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
		if(ticketGenerationResponse != null) {
			return ResponseEntity.ok().body(ticketGenerationResponse);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
