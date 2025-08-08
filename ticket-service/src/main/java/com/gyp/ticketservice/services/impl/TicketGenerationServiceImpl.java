package com.gyp.ticketservice.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.gyp.common.enums.event.TicketStatus;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.models.EventGenerationTicketEM;
import com.gyp.seatmapservice.grpc.seatmap.SeatMapRequest;
import com.gyp.ticketservice.dtos.seatmap.Row;
import com.gyp.ticketservice.dtos.seatmap.Seat;
import com.gyp.ticketservice.dtos.seatmap.SeatConfig;
import com.gyp.ticketservice.dtos.seatmap.SeatMapDto;
import com.gyp.ticketservice.dtos.seatmap.Section;
import com.gyp.ticketservice.dtos.seatmap.Table;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationResponseDto;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationSummaryDto;
import com.gyp.ticketservice.entities.TicketEntity;
import com.gyp.ticketservice.mappers.TicketMapper;
import com.gyp.ticketservice.messages.grpcs.SeatMapServiceGrpcClient;
import com.gyp.ticketservice.messages.producers.EventGeneratedProducer;
import com.gyp.ticketservice.repositories.TicketRepository;
import com.gyp.ticketservice.services.TicketGenerationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class TicketGenerationServiceImpl implements TicketGenerationService {
	private final TicketRepository ticketRepository;
	private final SeatMapServiceGrpcClient seatMapServiceGrpcClient;
	private final TicketMapper ticketMapper;
	private final EventGeneratedProducer eventGeneratedProducer;

	@Override
	public TicketGenerationSummaryDto validateTicket(String ticketNumber) {
		//		Optional<TicketEntity> ticketGeneration = ticketRepository.findByTicketNumber(ticketNumber);
		//		if(ticketGeneration.isPresent()) {
		//			LocalDateTime eventDateTime = ticketGeneration.get().getEventDateTime();
		//			LocalDateTime current = LocalDateTime.now();
		//			if(eventDateTime.toLocalDate().isEqual(current.toLocalDate())) {
		//				return ticketGenerationMapper.toSummary(ticketGeneration.get());
		//			}
		//		}
		return null;
	}

	@Override
	public TicketGenerationResponseDto getTicketGenerationById(String id) {
		//		Optional<TicketGenerationEntity> ticketGeneration = ticketGenerationRepository.findById(id);
		//		return ticketGeneration.map(ticketGenerationMapper::toResponse).orElse(null);
		return null;
	}

	@Override
	public void generateTicketBaseOnEventConfiguration(String eventId) {
		try {
			var request = SeatMapRequest.newBuilder().setEventId(eventId).build();
			SeatMapDto seatMapDto = seatMapServiceGrpcClient.getSeatMap(request);
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
		for(Seat seat : seats) {
			TicketEntity ticketEntity = TicketEntity.builder()
					.eventId(eventId)
					.eventName(seatMapDto.getEventName())
					.seatInfo(seatInfoPrefix + seat.getName())
					.ticketCode(generateTicketNumber())
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
