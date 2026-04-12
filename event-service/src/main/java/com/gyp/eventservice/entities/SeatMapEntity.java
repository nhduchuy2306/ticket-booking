package com.gyp.eventservice.entities;

import java.io.Serial;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SEATMAP")
public class SeatMapEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 5555409689753606585L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", length = 36)
	private String id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "venue_type", nullable = false)
	private String venueType;

	@Column(name = "organization_id", length = 36)
	private String organizationId;

	@Column(name = "seat_config", columnDefinition = "TEXT", nullable = false)
	private String seatConfigRaw;

	@Column(name = "stage_config", columnDefinition = "TEXT", nullable = false)
	private String stageConfigRaw;

	@JsonIgnore
	@OneToMany(mappedBy = "seatMapEntity", fetch = FetchType.LAZY)
	private List<VenueMapEntity> venueMapEntityList = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "seatMapEntity")
	private List<EventSectionMappingEntity> eventSectionMappingEntityList;
}
