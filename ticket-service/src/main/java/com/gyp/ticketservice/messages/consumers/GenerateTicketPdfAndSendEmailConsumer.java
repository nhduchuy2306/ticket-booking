package com.gyp.ticketservice.messages.consumers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gyp.common.annotations.KafkaComponent;
import com.gyp.common.converters.Serialization;
import com.gyp.common.enums.event.TicketStatus;
import com.gyp.common.kafkatopics.TopicConstants;
import com.gyp.common.models.EventGenerationPdfEM;
import com.gyp.common.services.UploadService;
import com.gyp.ticketservice.dtos.mail.TicketMailConfirmRequestDto;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationResponseDto;
import com.gyp.ticketservice.entities.ProcessedKafkaEventEntity;
import com.gyp.ticketservice.entities.TicketEntity;
import com.gyp.ticketservice.repositories.ProcessedKafkaEventRepository;
import com.gyp.ticketservice.repositories.TicketRepository;
import com.gyp.ticketservice.services.TicketCacheRedisService;
import com.gyp.ticketservice.services.PDFService;
import com.gyp.ticketservice.services.TicketDeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@KafkaComponent
@RequiredArgsConstructor
public class GenerateTicketPdfAndSendEmailConsumer {
	private static final String PDF_CONSUMER_NAME = "ticket.pdf.consumer";
	private static final String EMAIL_CONSUMER_NAME = "ticket.email.consumer";
	private static final String TICKET_BY_ID_PREFIX = "ticket:id:";
	private static final String TICKET_CODE_PREFIX = "ticket:code:";
	private static final String TICKET_GENERATION_PREFIX = "ticket:generation:";
	private static final String TICKETS_BY_EVENT_PREFIX = "ticket:event:";
	private static final String AVAILABLE_TICKETS_PREFIX = "ticket:available:event:";
	private static final String SEARCH_TICKETS_PREFIX = "ticket:search:";

	private final PDFService pdfService;
	private final TicketRepository ticketRepository;
	private final ProcessedKafkaEventRepository processedKafkaEventRepository;
	private final UploadService uploadService;
	private final TicketDeliveryService ticketDeliveryService;
	private final TicketCacheRedisService ticketCacheRedisService;

	@KafkaListener(topics = TopicConstants.GENERATE_TICKET_PDF_EVENT)
	public void generateTicketPdf(String message) {
		log.info("Received GenerateTicketPdf event: {}", message);
		EventGenerationPdfEM eventGenerationPdfEM = Serialization.deserializeFromString(message,
				EventGenerationPdfEM.class);
		if(StringUtils.isEmpty(eventGenerationPdfEM.getSeatId())) {
			log.warn("Seat ID is empty in SendEmail event: {}", message);
			return;
		}
		String dedupKey = resolveDedupKey(eventGenerationPdfEM.getIdempotencyKey(), eventGenerationPdfEM.getOrderId(),
				eventGenerationPdfEM.getSeatId(), eventGenerationPdfEM.getSeatIds());
		String eventKey = dedupKey + ":" + eventGenerationPdfEM.getSeatId();
		if(processedKafkaEventRepository.existsByConsumerNameAndEventKey(PDF_CONSUMER_NAME, eventKey)) {
			log.info("Skipping duplicate PDF event: {}", eventKey);
			return;
		}
		TicketEntity ticketEntity = ensureTicketExists(eventGenerationPdfEM, eventGenerationPdfEM.getSeatId(), 0);
		TicketGenerationResponseDto ticketGenerationResponseDto = toTicketGenerationResponseDto(ticketEntity,
				eventGenerationPdfEM, 0);
		var pdf = pdfService.generateTicketPDF(ticketGenerationResponseDto);
		var uploadFile = uploadService.upload(pdf, "ticket.pdf", "application/pdf");
		ticketEntity.setPdfUrl(uploadFile.getLeft());
		ticketEntity.setAttendeeEmail(eventGenerationPdfEM.getCustomerEmail());
		ticketEntity.setAttendeeName(eventGenerationPdfEM.getCustomerEmail());
		ticketEntity.setReservedDateTime(LocalDateTime.now());
		ticketRepository.save(ticketEntity);
		evictTicketCaches(eventGenerationPdfEM.getId(), List.of(ticketEntity));
		processedKafkaEventRepository.save(ProcessedKafkaEventEntity.builder()
				.consumerName(PDF_CONSUMER_NAME)
				.eventKey(eventKey)
				.processedAt(LocalDateTime.now())
				.build());

	}

	@KafkaListener(topics = TopicConstants.SEND_EMAIL_EVENT)
	public void sendEmail(String message) {
		log.info("Received SendEmail event: {}", message);
		EventGenerationPdfEM eventGenerationPdfEM = Serialization.deserializeFromString(message,
				EventGenerationPdfEM.class);
		String seatKey = eventGenerationPdfEM.getSeatIds() == null ? "" : String.join(",",
				eventGenerationPdfEM.getSeatIds().stream().sorted().toList());
		String dedupKey = resolveDedupKey(eventGenerationPdfEM.getIdempotencyKey(),
				eventGenerationPdfEM.getOrderId(),
				eventGenerationPdfEM.getSeatId(), eventGenerationPdfEM.getSeatIds());
		String eventKey = dedupKey + ":" + seatKey;
		if(processedKafkaEventRepository.existsByConsumerNameAndEventKey(EMAIL_CONSUMER_NAME, eventKey)) {
			log.info("Skipping duplicate email event: {}", eventKey);
			return;
		}
		if(eventGenerationPdfEM.getSeatIds() == null || eventGenerationPdfEM.getSeatIds().isEmpty()) {
			log.warn("Seat IDs are empty in SendEmail event: {}", message);
			return;
		}
		ensureTicketsExist(eventGenerationPdfEM);

		List<byte[]> pdfs = new ArrayList<>();
		List<String> ticketIds = new ArrayList<>();
		List<TicketEntity> ticketEntities = new ArrayList<>();
		List<String> seatIds = eventGenerationPdfEM.getSeatIds();
		List<TicketGenerationResponseDto> ticketGenerationResponseDtoList = new ArrayList<>();
		for(int index = 0; index < seatIds.size(); index++) {
			String seatId = seatIds.get(index);
			TicketEntity ticketEntity = ticketRepository.findBySeatId(seatId)
					.orElseThrow(() -> new RuntimeException("Ticket not found for seatId: " + seatId));
			ticketEntities.add(ticketEntity);
			ticketIds.add(ticketEntity.getId());

			TicketGenerationResponseDto ticketGenerationResponseDto = toTicketGenerationResponseDto(ticketEntity,
					eventGenerationPdfEM, index);
			ticketGenerationResponseDtoList.add(ticketGenerationResponseDto);

			var pdf = pdfService.generateTicketPDF(ticketGenerationResponseDto);
			pdfs.add(pdf);
		}

		TicketMailConfirmRequestDto ticketMailConfirmRequestDto = TicketMailConfirmRequestDto.builder()
				.ticketIds(ticketIds)
				.hasQrCode(true)
				.hasTickPdfAttachment(true)
				.build();

		ticketDeliveryService.sendByEmailWithAttachment(ticketMailConfirmRequestDto, pdfs,
				ticketGenerationResponseDtoList);
		evictTicketCaches(eventGenerationPdfEM.getId(), ticketEntities);
		processedKafkaEventRepository.save(ProcessedKafkaEventEntity.builder()
				.consumerName(EMAIL_CONSUMER_NAME)
				.eventKey(eventKey)
				.processedAt(LocalDateTime.now())
				.build());
	}

	private TicketEntity ensureTicketExists(EventGenerationPdfEM eventGenerationPdfEM, String seatId, int index) {
		return ticketRepository.findBySeatId(seatId)
				.orElseGet(() -> ticketRepository.save(TicketEntity.builder()
						.ticketCode(generateTicketCode(eventGenerationPdfEM.getId(), seatId))
						.eventId(eventGenerationPdfEM.getId())
						.eventName(eventGenerationPdfEM.getName())
						.organizationId(null)
						.eventDateTime(eventGenerationPdfEM.getStartTime())
						.seatId(seatId)
						.seatInfo(resolveSeatInfo(eventGenerationPdfEM, index, seatId))
						.status(TicketStatus.COMING_SOON)
						.attendeeName(eventGenerationPdfEM.getCustomerEmail())
						.attendeeEmail(eventGenerationPdfEM.getCustomerEmail())
						.build()));
	}

	private void ensureTicketsExist(EventGenerationPdfEM eventGenerationPdfEM) {
		for(int index = 0; index < eventGenerationPdfEM.getSeatIds().size(); index++) {
			String seatId = eventGenerationPdfEM.getSeatIds().get(index);
			ensureTicketExists(eventGenerationPdfEM, seatId, index);
		}
	}

	private TicketGenerationResponseDto toTicketGenerationResponseDto(TicketEntity ticketEntity,
			EventGenerationPdfEM eventGenerationPdfEM, int index) {
		return TicketGenerationResponseDto.builder()
				.id(ticketEntity.getId())
				.ticketNumber(ticketEntity.getTicketCode())
				.eventId(eventGenerationPdfEM.getId())
				.eventName(eventGenerationPdfEM.getName())
				.seatId(ticketEntity.getSeatId())
				.seatInfo(ticketEntity.getSeatInfo())
				.seatLabel(resolveSeatLabel(eventGenerationPdfEM, index, ticketEntity.getSeatInfo()))
				.sectionId(resolveSeatValue(eventGenerationPdfEM.getSectionIds(), index))
				.rowId(resolveSeatValue(eventGenerationPdfEM.getRowIds(), index))
				.ticketTypeId(resolveSeatValue(eventGenerationPdfEM.getTicketTypeIds(), index))
				.attendeeName(eventGenerationPdfEM.getCustomerEmail())
				.attendeeEmail(eventGenerationPdfEM.getCustomerEmail())
				.eventDateTime(eventGenerationPdfEM.getStartTime())
				.status(TicketStatus.COMING_SOON)
				.build();
	}

	private String resolveSeatInfo(EventGenerationPdfEM eventGenerationPdfEM, int index, String fallbackSeatId) {
		String seatLabel = resolveSeatLabel(eventGenerationPdfEM, index, fallbackSeatId);
		return StringUtils.defaultIfBlank(seatLabel, fallbackSeatId);
	}

	private String resolveSeatLabel(EventGenerationPdfEM eventGenerationPdfEM, int index, String fallback) {
		return resolveSeatValue(eventGenerationPdfEM.getSeatLabels(), index, fallback);
	}

	private String resolveSeatValue(List<String> values, int index) {
		return resolveSeatValue(values, index, null);
	}

	private String resolveSeatValue(List<String> values, int index, String fallback) {
		if(values == null || index < 0 || index >= values.size()) {
			return fallback;
		}
		return values.get(index);
	}

	private String generateTicketCode(String eventId, String seatId) {
		return eventId + "-" + seatId + "-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	}

	private String resolveDedupKey(String idempotencyKey, String orderId, String seatId, List<String> seatIds) {
		if(StringUtils.isNotEmpty(idempotencyKey)) {
			return idempotencyKey;
		}
		if(StringUtils.isNotEmpty(orderId)) {
			return orderId;
		}
		if(StringUtils.isNotEmpty(seatId)) {
			return seatId;
		}
		if(seatIds != null && !seatIds.isEmpty()) {
			return String.join(",", seatIds.stream().sorted().toList());
		}
		return "legacy-message";
	}

	private void evictTicketCaches(String eventId, List<TicketEntity> tickets) {
		ticketCacheRedisService.evictByPrefix(TICKETS_BY_EVENT_PREFIX + eventId);
		ticketCacheRedisService.evictByPrefix(AVAILABLE_TICKETS_PREFIX + eventId + ":");
		ticketCacheRedisService.evictByPrefix(SEARCH_TICKETS_PREFIX);
		if(tickets == null) {
			return;
		}

		for(TicketEntity ticket : tickets) {
			ticketCacheRedisService.evict(TICKET_BY_ID_PREFIX + ticket.getId());
			ticketCacheRedisService.evict(TICKET_GENERATION_PREFIX + ticket.getId());
			ticketCacheRedisService.evict(TICKET_CODE_PREFIX + ticket.getTicketCode());
		}
	}
}
