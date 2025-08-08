package com.gyp.ticketservice.entities;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gyp.common.enums.event.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@Table(name = "TICKETTYPE")
@AllArgsConstructor
@NoArgsConstructor
public class TicketTypeEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 4789466346277152613L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "color")
	private String color;

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private Double price;

	@Column(name = "quantity_available")
	private Integer quantityAvailable;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private TicketStatus status;

	@Column(name = "sale_start_date")
	private LocalDateTime saleStartDate;

	@Column(name = "sale_end_date")
	private LocalDateTime saleEndDate;

	@Column(name = "organization_id")
	private String organizationId;

	@Column(name = "event_id")
	private String eventId;

	@OneToMany(mappedBy = "ticketTypeEntity", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<TicketEntity> ticketEntityList = new ArrayList<>();
}
