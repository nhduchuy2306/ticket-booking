package com.gyp.ticketservice.entities;

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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "TICKETGENERATION")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TicketGenerationEntity extends AbstractEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "event_id", nullable = false)
	private String eventId;

	@Column(name = "event_name")
	private String eventName;

	@Column(name = "seat_info", nullable = false)
	private String seatInfo;

	@Column(name = "ticket_number", unique = true)
	private String ticketNumber;

	@Column(name = "attendee_name")
	private String attendeeName;

	@Column(name = "attendee_email")
	private String attendeeEmail;

	@Column(name = "event_date_time")
	private LocalDateTime eventDateTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private TicketStatus status;

	@ManyToOne
	@JoinColumn(name = "ticket_id", nullable = false)
	private TicketEntity ticketEntity;
}
