package com.gyp.ticket.eventservice.entities;

import java.io.Serial;
import java.math.BigDecimal;
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
@Table(name = "TICKETTYPE")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TicketTypeEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 4789466346277152613L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private double price;

	@Column(name = "quantity_available")
	private Integer quantityAvailable;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private TicketStatus status;

	@Column(name = "sale_start_date")
	private LocalDateTime saleStartDate;

	@Column(name = "sale_end_date")
	private LocalDateTime saleEndDate;

	@ManyToOne
	@JoinColumn(name = "event_id")
	private EventEntity eventEntity;
}
