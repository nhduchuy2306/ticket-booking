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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "VENUEMAP")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VenueMapEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = -4773896025971427280L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", length = 36)
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "width")
	private Double width;

	@Column(name = "height")
	private Double height;

	@Column(name = "organization_id", length = 36)
	private String organizationId;

	@ManyToOne
	@JoinColumn(name = "venue_id")
	private VenueEntity venueEntity;

	@ManyToOne
	@JoinColumn(name = "seat_map_id")
	private SeatMapEntity seatMapEntity;

	@JsonIgnore
	@OneToMany(mappedBy = "venueMapEntity", fetch = FetchType.LAZY)
	private List<EventEntity> eventEntityList = new ArrayList<>();

}
