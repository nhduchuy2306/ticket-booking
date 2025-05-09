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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "TICKET")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TicketEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = -5630442615915968220L;

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

	@Column(name = "reserved_date")
	private LocalDateTime reservedDateTime;

	@ManyToOne
	@JoinColumn(name = "ticket_type_id", nullable = false)
	private TicketTypeEntity ticketTypeEntity;
}
