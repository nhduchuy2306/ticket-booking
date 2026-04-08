package com.gyp.eventservice.entities;

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
@Table(name = "seat_hold", uniqueConstraints = {
		@jakarta.persistence.UniqueConstraint(name = "uk_seat_hold_event_token_seat",
				columnNames = {"event_id", "hold_token", "seat_id"})
})
public class SeatHoldEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 8106708175560502142L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "event_id", nullable = false)
	private String eventId;

	@Column(name = "hold_token", nullable = false)
	private String holdToken;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "expires_at", nullable = false)
	private LocalDateTime expiresAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private SeatHoldStatus status;

	@ManyToOne(optional = false)
	@JoinColumn(name = "seat_id", nullable = false)
	private SeatEntity seatEntity;
}