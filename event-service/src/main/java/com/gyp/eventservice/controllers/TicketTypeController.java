package com.gyp.eventservice.controllers;

import java.util.Optional;

import com.gyp.common.controllers.AbstractController;
import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.eventservice.dtos.tickettype.TicketTypeRequestDto;
import com.gyp.eventservice.services.TicketTypeService;
import com.gyp.eventservice.services.criteria.TicketTypeSearchCriteria;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(TicketTypeController.TICKET_TYPE_CONTROLLER_PATH)
public class TicketTypeController extends AbstractController {
	public static final String TICKET_TYPE_CONTROLLER_PATH = "/ticket-types";

	private final TicketTypeService ticketTypeService;

	@GetMapping
	public ResponseEntity<?> getTicketTypes(
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size) {
		String organizationId = getCurrentOrganizationId();
		TicketTypeSearchCriteria criteria = TicketTypeSearchCriteria.builder()
				.organizationId(organizationId)
				.build();
		PaginatedDto pagination = PaginatedDto.builder()
				.page(page.orElse(0))
				.size(size.orElse(10))
				.build();
		return ResponseEntity.ok(ticketTypeService.getTicketTypes(criteria, pagination));
	}

	@GetMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> getTicketTypeById(@PathVariable("id") String id) {
		return ResponseEntity.ok(ticketTypeService.getTicketTypeById(id));
	}

	@PostMapping
	public ResponseEntity<?> createTicketType(@RequestBody TicketTypeRequestDto ticketTypeRequestDto) {
		var ticketType = ticketTypeService.createTicketType(ticketTypeRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(ticketType);
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
