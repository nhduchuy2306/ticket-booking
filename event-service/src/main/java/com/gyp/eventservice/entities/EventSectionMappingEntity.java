package com.gyp.eventservice.entities;

import java.io.Serial;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table(name = "EVENTSECTIONMAPPING")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventSectionMappingEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = -7061751047213833585L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", length = 36)
	private String id;

	@ManyToOne
	@JoinColumn(name = "event_id", nullable = false)
	private EventEntity eventEntity;

	@ManyToOne
	@JoinColumn(name = "ticket_type_id", nullable = false)
	private TicketTypeEntity ticketTypeEntity;

	@ManyToOne
	@JoinColumn(name = "seat_map_id")
	private SeatMapEntity seatMapEntity;

	@Column(name = "section_id")
	private String sectionId;
}

