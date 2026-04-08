package com.gyp.ticketservice.messages.consumers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.common.annotations.KafkaComponent;
import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.TopicConstants;
import com.gyp.common.models.EventGenerationPdfEM;
import com.gyp.common.services.UploadService;
import com.gyp.ticketservice.dtos.mail.TicketMailConfirmRequestDto;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationResponseDto;
import com.gyp.ticketservice.entities.TicketEntity;
import com.gyp.ticketservice.entities.ProcessedKafkaEventEntity;
import com.gyp.ticketservice.repositories.TicketRepository;
import com.gyp.ticketservice.repositories.ProcessedKafkaEventRepository;
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

	private final PDFService pdfService;
	private final TicketRepository ticketRepository;
	private final ProcessedKafkaEventRepository processedKafkaEventRepository;
	private final UploadService uploadService;
	private final TicketDeliveryService ticketDeliveryService;

	@KafkaListener(topics = TopicConstants.GENERATE_TICKET_PDF_EVENT)
	public void generateTicketPdf(String message) {
		try {
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
			TicketEntity ticketEntity = ticketRepository.findBySeatId(eventGenerationPdfEM.getSeatId())
					.orElseThrow(() -> new RuntimeException(
							"Ticket not found for seatId: " + eventGenerationPdfEM.getSeatId()));
			TicketGenerationResponseDto ticketGenerationResponseDto = TicketGenerationResponseDto.builder()
					.id(ticketEntity.getId())
					.ticketNumber(ticketEntity.getTicketCode())
					.eventId(eventGenerationPdfEM.getId())
					.eventName(eventGenerationPdfEM.getName())
					.seatId(ticketEntity.getSeatId())
					.seatInfo(ticketEntity.getSeatInfo())
					.attendeeName(eventGenerationPdfEM.getCustomerEmail())
					.attendeeEmail(eventGenerationPdfEM.getCustomerEmail())
					.eventDateTime(eventGenerationPdfEM.getStartTime())
					.build();
			var pdf = pdfService.generateTicketPDF(ticketGenerationResponseDto);
			var uploadFile = uploadService.upload(pdf, "ticket.pdf", "application/pdf");
			ticketEntity.setPdfUrl(uploadFile.getLeft());
			ticketEntity.setAttendeeEmail(eventGenerationPdfEM.getCustomerEmail());
			ticketEntity.setAttendeeName(eventGenerationPdfEM.getCustomerEmail());
			ticketEntity.setReservedDateTime(LocalDateTime.now());
			ticketRepository.save(ticketEntity);
			processedKafkaEventRepository.save(ProcessedKafkaEventEntity.builder()
					.consumerName(PDF_CONSUMER_NAME)
					.eventKey(eventKey)
					.processedAt(LocalDateTime.now())
					.build());
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@KafkaListener(topics = TopicConstants.SEND_EMAIL_EVENT)
	public void sendEmail(String message) {
		try {
			log.info("Received SendEmail event: {}", message);
			EventGenerationPdfEM eventGenerationPdfEM = Serialization.deserializeFromString(message,
					EventGenerationPdfEM.class);
			String seatKey = eventGenerationPdfEM.getSeatIds() == null ? "" : String.join(",",
					eventGenerationPdfEM.getSeatIds().stream().sorted().toList());
			String dedupKey = resolveDedupKey(eventGenerationPdfEM.getIdempotencyKey(), eventGenerationPdfEM.getOrderId(),
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

			List<byte[]> pdfs = new ArrayList<>();
			List<String> ticketIds = new ArrayList<>();
			List<String> seatIds = eventGenerationPdfEM.getSeatIds();
			List<TicketGenerationResponseDto> ticketGenerationResponseDtoList = new ArrayList<>();
			seatIds.forEach(seatId -> {
				TicketEntity ticketEntity = ticketRepository.findBySeatId(seatId)
						.orElseThrow(() -> new RuntimeException(
								"Ticket not found for seatId: " + eventGenerationPdfEM.getSeatId()));
				ticketIds.add(ticketEntity.getId());

				TicketGenerationResponseDto ticketGenerationResponseDto = TicketGenerationResponseDto.builder()
						.id(ticketEntity.getId())
						.ticketNumber(ticketEntity.getTicketCode())
						.eventId(eventGenerationPdfEM.getId())
						.eventName(eventGenerationPdfEM.getName())
						.seatId(ticketEntity.getSeatId())
						.seatInfo(ticketEntity.getSeatInfo())
						.attendeeName(eventGenerationPdfEM.getCustomerEmail())
						.attendeeEmail(eventGenerationPdfEM.getCustomerEmail())
						.eventDateTime(eventGenerationPdfEM.getStartTime())
						.build();
				ticketGenerationResponseDtoList.add(ticketGenerationResponseDto);

				var pdf = pdfService.generateTicketPDF(ticketGenerationResponseDto);
				pdfs.add(pdf);
			});

			TicketMailConfirmRequestDto ticketMailConfirmRequestDto = TicketMailConfirmRequestDto.builder()
					.ticketIds(ticketIds)
					.hasQrCode(true)
					.hasTickPdfAttachment(true)
					.build();

			ticketDeliveryService.sendByEmailWithAttachment(ticketMailConfirmRequestDto, pdfs,
					ticketGenerationResponseDtoList);
			processedKafkaEventRepository.save(ProcessedKafkaEventEntity.builder()
					.consumerName(EMAIL_CONSUMER_NAME)
					.eventKey(eventKey)
					.processedAt(LocalDateTime.now())
					.build());
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
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
}
