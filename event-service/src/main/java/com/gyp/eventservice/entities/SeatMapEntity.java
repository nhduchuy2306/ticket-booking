package com.gyp.eventservice.entities;

import java.io.Serial;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SEATMAP")
@EqualsAndHashCode(callSuper = true)
public class SeatMapEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 5555409689753606585L;

	@Id
	@Column(name = "id", nullable = false)
	private String id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "venue_type", nullable = false)
	private String venueType;

	@Column(name = "seat_config", columnDefinition = "TEXT", nullable = false)
	private String seatConfigRaw;

	@Column(name = "stage_config", columnDefinition = "TEXT", nullable = false)
	private String stageConfigRaw;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "venue_id", nullable = false)
	private VenueEntity venueEntity;
}
