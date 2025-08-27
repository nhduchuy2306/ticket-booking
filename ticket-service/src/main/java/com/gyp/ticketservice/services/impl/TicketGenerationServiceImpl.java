package com.gyp.ticketservice.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.gyp.common.enums.event.TicketStatus;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.models.EventGenerationTicketEM;
import com.gyp.common.utils.SecurityUtils;
import com.gyp.ticketservice.dtos.seatmap.Row;
import com.gyp.ticketservice.dtos.seatmap.Seat;
import com.gyp.ticketservice.dtos.seatmap.SeatConfig;
import com.gyp.ticketservice.dtos.seatmap.SeatMapDto;
import com.gyp.ticketservice.dtos.seatmap.Section;
import com.gyp.ticketservice.dtos.seatmap.Table;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationResponseDto;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationSummaryDto;
import com.gyp.ticketservice.entities.TicketEntity;
import com.gyp.ticketservice.messages.producers.EventGeneratedProducer;
import com.gyp.ticketservice.repositories.TicketRepository;
import com.gyp.ticketservice.services.TicketGenerationService;
import com.gyp.ticketservice.services.criteria.TicketSearchCriteria;
import com.gyp.ticketservice.services.specification.TicketSpecification;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class TicketGenerationServiceImpl implements TicketGenerationService {
	private final TicketRepository ticketRepository;
	private final EventGeneratedProducer eventGeneratedProducer;

	@Override
	public TicketGenerationSummaryDto validateTicket(String ticketNumber) {
		return null;
	}

	@Override
	public TicketGenerationResponseDto getTicketGenerationById(String id) {
		//		Optional<TicketGenerationEntity> ticketGeneration = ticketGenerationRepository.findById(id);
		//		return ticketGeneration.map(ticketGenerationMapper::toResponse).orElse(null);
		return null;
	}

	@Override
	public void generateTicketBaseOnEventConfiguration(SeatMapDto seatMapDto, String eventId) {
		try {
			SeatConfig seatConfig = seatMapDto.getSeatConfig();

			List<Section> sections = seatConfig.getSections();
			if(CollectionUtils.isEmpty(sections)) {
				throw new ResourceNotFoundException("No sections found in seat configuration for event: " + eventId);
			}

			for(Section section : sections) {
				List<Row> rows = section.getRows();
				List<Table> tables = section.getTables();
				if(!CollectionUtils.isEmpty(rows)) {
					generateRowTickets(eventId, seatMapDto, section, rows);
				}
				if(!CollectionUtils.isEmpty(tables)) {
					generateTableTickets(eventId, seatMapDto, section, tables);
				}
			}
			EventGenerationTicketEM eventGenerationTicketEM = EventGenerationTicketEM.builder()
					.isTicketGenerated(true)
					.eventId(eventId)
					.build();
			eventGeneratedProducer.send(eventGenerationTicketEM);
		} catch(Exception e) {
			throw new ResourceNotFoundException("Error generating tickets for event: " + eventId, e);
		}
	}

	@Override
	public void deleteTicketsGeneration(String eventId) {
		try {
			String organizationId = SecurityUtils.getCurrentOrganizationId();
			TicketSearchCriteria ticketSearchCriteria = TicketSearchCriteria.builder()
					.eventId(eventId)
					.organizationId(organizationId)
					.build();
			Specification<TicketEntity> specification = TicketSpecification.createTicketSpecification(
					ticketSearchCriteria);
			var tickets = ticketRepository.findAll(specification);
			if(!CollectionUtils.isEmpty(tickets)) {
				ticketRepository.deleteAll(tickets);
				EventGenerationTicketEM eventGenerationTicketEM = EventGenerationTicketEM.builder()
						.isTicketGenerated(false)
						.eventId(eventId)
						.build();
				eventGeneratedProducer.send(eventGenerationTicketEM);
			}
		} catch(Exception e) {
			throw new ResourceNotFoundException("Error deleting tickets for event: " + eventId, e);
		}
	}

	private void generateRowTickets(String eventId, SeatMapDto seatMapDto, Section section, List<Row> rows) {
		for(Row row : rows) {
			List<Seat> seats = row.getSeats();
			if(CollectionUtils.isEmpty(seats)) {
				throw new ResourceNotFoundException(
						"No seats found in row: " + row.getName() + " for section: " + section.getName());
			}
			String seatInfo = section.getName() + "-" + row.getName() + "-";
			generateSeatTickets(eventId, seatMapDto, seats, seatInfo);
		}
	}

	private void generateTableTickets(String eventId, SeatMapDto seatMapDto, Section section, List<Table> tables) {
		for(Table table : tables) {
			List<Seat> seats = table.getSeats();
			if(CollectionUtils.isEmpty(seats)) {
				throw new ResourceNotFoundException(
						"No seats found in table: " + table.getName() + " for section: " + section.getName());
			}
			String seatInfo = section.getName() + "-" + table.getName() + "-";
			generateSeatTickets(eventId, seatMapDto, seats, seatInfo);
		}
	}

	private void generateSeatTickets(String eventId, SeatMapDto seatMapDto, List<Seat> seats, String seatInfoPrefix) {
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		for(Seat seat : seats) {
			TicketEntity ticketEntity = TicketEntity.builder()
					.eventId(eventId)
					.eventName(seatMapDto.getEventName())
					.seatInfo(seatInfoPrefix + seat.getName())
					.ticketCode(generateTicketNumber())
					.organizationId(organizationId)
					.eventDateTime(StringUtils.isNotEmpty(seatMapDto.getEventDateTime())
							? LocalDateTime.parse(seatMapDto.getEventDateTime())
							: LocalDateTime.now())
					.status(TicketStatus.COMING_SOON)
					.build();
			ticketRepository.save(ticketEntity);
		}
	}

	private String generateTicketNumber() {
		return "TICKET-" + System.currentTimeMillis();
	}
}
