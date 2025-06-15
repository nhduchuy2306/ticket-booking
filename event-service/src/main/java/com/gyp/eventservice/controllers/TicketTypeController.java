package com.gyp.eventservice.controllers;

import com.gyp.common.controllers.AbstractController;
import com.gyp.eventservice.dtos.tickettype.TicketTypeRequestDto;
import com.gyp.eventservice.services.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(TicketTypeController.TICKET_TYPE_CONTROLLER_PATH)
public class TicketTypeController extends AbstractController {
	public static final String TICKET_TYPE_CONTROLLER_PATH = "/ticket-types";

	private final TicketTypeService ticketTypeService;

	@GetMapping
	public ResponseEntity<?> getTicketTypes() {
		return ResponseEntity.ok(ticketTypeService.getTicketTypes());
	}

	@GetMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> getTicketTypeById(@PathVariable("id") String id) {
		return ResponseEntity.ok(ticketTypeService.getTicketTypeById(id));
	}

	@PostMapping
	public ResponseEntity<?> createTicketType(@RequestBody TicketTypeRequestDto ticketTypeRequestDto) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ticketTypeService.createTicketType(ticketTypeRequestDto));
	}

	@PutMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> updateTicketTypeById(@PathVariable("id") String id,
			@RequestBody TicketTypeRequestDto requestDto) {
		return ResponseEntity.ok(ticketTypeService.updateTicketType(id, requestDto));
	}

	@DeleteMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> deleteTicketTypeById(@PathVariable("id") String id) {
		ticketTypeService.deleteTicketType(id);
		return ResponseEntity.noContent().build();
	}
}
