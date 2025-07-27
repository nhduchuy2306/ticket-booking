package com.gyp.eventservice.entities;

import java.io.Serial;
import java.time.LocalDateTime;

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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Table(name = "EVENTPROMOTION")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventPromotionEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 8462377661469758093L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "code")
	private String code;

	@Column(name = "discount_amount")
	private double discountAmount;

	@Column(name = "valid_from")
	private LocalDateTime validFrom;

	@Column(name = "valid_to")
	private LocalDateTime validTo;

	@Column(name = "organization_id")
	private String organizationId;

	@ManyToOne
	@JoinColumn(name = "event_id")
	private EventEntity eventEntity;
}
