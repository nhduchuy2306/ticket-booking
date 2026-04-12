package com.gyp.eventservice.entities;

import java.io.Serial;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "STANDINVENTORY", uniqueConstraints = {
		@UniqueConstraint(name = "uk_stand_event_section_ticket_type",
				columnNames = { "event_id", "section_id", "ticket_type_id" })
})
public class StandInventoryEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 2864641267934899561L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", length = 36)
	private String id;

	@Column(name = "event_id", nullable = false)
	private String eventId;

	@Column(name = "section_id", nullable = false)
	private String sectionId;

	@Column(name = "ticket_type_id", nullable = false)
	private String ticketTypeId;

	@Column(name = "total_capacity", nullable = false)
	private int totalCapacity;

	@Column(name = "reserved_quantity", nullable = false)
	private int reservedQuantity;

	@Column(name = "sold_quantity", nullable = false)
	private int soldQuantity;

	@Version
	@Column(name = "version")
	private Long version;
}
