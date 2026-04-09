package com.gyp.eventservice.entities;

import java.io.Serial;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SEAT", uniqueConstraints = {
		@UniqueConstraint(name = "uk_seat_event_key", columnNames = {"event_id", "seat_key"})
})
public class SeatEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = -8110091261913840521L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "event_id", nullable = false)
	private String eventId;

	@Column(name = "seat_key", nullable = false)
	private String seatKey;

	@Column(name = "seat_label")
	private String seatLabel;

	@Column(name = "section_id")
	private String sectionId;

	@Column(name = "row_id")
	private String rowId;

	@Column(name = "ticket_type_id")
	private String ticketTypeId;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private SeatInventoryStatus status;

	@Version
	@Column(name = "version")
	private Long version;
}