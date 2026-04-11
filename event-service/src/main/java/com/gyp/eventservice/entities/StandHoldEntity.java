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
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

import com.gyp.common.enums.event.SeatHoldStatus;
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
@Table(name = "STANDHOLD", uniqueConstraints = {
		@UniqueConstraint(name = "uk_stand_hold_event_token_inventory",
				columnNames = { "event_id", "hold_token", "stand_inventory_id" })
})
public class StandHoldEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = -2496102801410282261L;

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

	@Builder.Default
	@Column(name = "quantity", nullable = false)
	private int quantity = 1;

	@Column(name = "expires_at", nullable = false)
	private LocalDateTime expiresAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private SeatHoldStatus status;

	@ManyToOne(optional = false)
	@JoinColumn(name = "stand_inventory_id", nullable = false)
	private StandInventoryEntity standInventoryEntity;

	@Version
	@Column(name = "version")
	private Long version;
}