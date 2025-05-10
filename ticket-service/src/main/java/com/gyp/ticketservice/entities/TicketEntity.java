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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "TICKET")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TicketEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = -5630442615915968220L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "event_id", nullable = false)
	private String eventId;

	@Column(name = "event_name")
	private String eventName;

	@Column(name = "event_date_time")
	private LocalDateTime eventDateTime;

	@Column(name = "reserved_date")
	private LocalDateTime reservedDateTime;

	@ManyToOne
	@JoinColumn(name = "ticket_type_id", nullable = false)
	private TicketTypeEntity ticketTypeEntity;

	@OneToMany(mappedBy = "ticketEntity", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<TicketGenerationEntity> ticketGenerationEntityList = new ArrayList<>();
}
