package com.gyp.ticketservice.entities;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "TICKETTYPE")
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

	@Column(name = "event_id", nullable = false)
	private String eventId;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private double price;

	@Column(name = "quantity_available")
	private Integer quantityAvailable;

	@Column(name = "sale_start_date")
	private LocalDateTime saleStartDate;

	@Column(name = "sale_end_date")
	private LocalDateTime saleEndDate;

	@OneToMany(mappedBy = "ticketTypeEntity", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<TicketEntity> ticketEntityList = new ArrayList<>();
}
