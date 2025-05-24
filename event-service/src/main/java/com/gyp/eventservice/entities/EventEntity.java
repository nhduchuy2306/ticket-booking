package com.gyp.eventservice.entities;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gyp.common.enums.event.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Table(name = "EVENT")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 8909226582016076494L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private EventStatus status;

	@Embedded
	private EventTimeEmbeddable time;

	@ManyToOne
	@JoinColumn(name = "organizer_id")
	private OrganizerEntity organizerEntity;

	@ManyToOne()
	@JoinColumn(name = "venue_id")
	private VenueEntity venueEntity;

	@JsonIgnore
	@OneToMany(mappedBy = "eventEntity", cascade = CascadeType.ALL)
	private List<TicketTypeEntity> ticketTypeEntityList = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "eventEntity", cascade = CascadeType.ALL)
	private List<EventPromotionEntity> eventPromotionEntityList = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "eventEntity", cascade = CascadeType.ALL)
	private List<EventApprovalEntity> eventApprovalEntityList = new ArrayList<>();

	@ManyToMany
	@JoinTable(
			name = "eventcategories",
			joinColumns = @JoinColumn(name = "event_id"),
			inverseJoinColumns = @JoinColumn(name = "category_id")
	)
	private List<CategoryEntity> categoryEntityList = new ArrayList<>();
}
