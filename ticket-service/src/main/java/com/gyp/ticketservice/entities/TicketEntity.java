package com.gyp.ticketservice.entities;

import java.io.Serial;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.gyp.common.enums.event.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@Table(name = "ticket")
@AllArgsConstructor
@NoArgsConstructor
public class TicketEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = -5630442615915968220L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "ticket_code", unique = true)
	private String ticketCode;

	@Column(name = "event_id", nullable = false)
	private String eventId;

	@Column(name = "event_name")
	private String eventName;

	@Column(name = "organization_id")
	private String organizationId;

	@Column(name = "event_date_time")
	private LocalDateTime eventDateTime;

	@Column(name = "reserved_date")
	private LocalDateTime reservedDateTime;

	@ManyToOne
	@JoinColumn(name = "ticket_type_id")
	private TicketTypeEntity ticketTypeEntity;

	@Column(name = "seat_info")
	private String seatInfo;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private TicketStatus status;

	@Column(name = "attendee_name")
	private String attendeeName;

	@Column(name = "attendee_email")
	private String attendeeEmail;

	@Column(name = "qr_code_url", columnDefinition = "TEXT")
	private String qrCodeUrl;

	@Column(name = "pdf_url", columnDefinition = "TEXT")
	private String pdfUrl;
}
