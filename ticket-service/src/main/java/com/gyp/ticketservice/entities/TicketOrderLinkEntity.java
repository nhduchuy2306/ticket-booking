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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@Table(name = "TICKETORDERLINK")
@AllArgsConstructor
@NoArgsConstructor
public class TicketOrderLinkEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = -5610487937942484351L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "ticket_id", nullable = false)
	private String ticketId;

	@Column(name = "order_id", nullable = false)
	private String orderId;

	@Column(name = "purchase_time")
	private LocalDateTime purchaseTime;
}

