package com.gyp.ticketservice.entities;

import java.io.Serial;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "TICKET")
@AllArgsConstructor
@NoArgsConstructor
public class TicketEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = -5630442615915968220L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "event_name")
	private String eventName;

	@Column(name = "attendee_name")
	private String attendeeName;

	@Column(name = "attendee_email")
	private String attendeeEmail;

	@Column(name = "event_date_time")
	private LocalDateTime eventDateTime;

	@Column(name = "ticket_type")
	private String ticketType;

	@Column(name = "seat_info")
	private String seatInfo;

	@Column(name = "ticket_number")
	private String ticketNumber;
}
