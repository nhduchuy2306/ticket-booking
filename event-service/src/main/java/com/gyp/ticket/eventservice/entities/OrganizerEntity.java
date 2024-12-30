package com.gyp.ticket.eventservice.entities;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "ORGANIZER")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrganizerEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 1705971957802863896L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "user_name")
	private String username;

	@Column(name = "dob")
	private LocalDateTime dob;

	@Column(name = "phone_number")
	private String phoneNumber;

	@JsonIgnore
	@OneToMany(mappedBy = "organizerEntity")
	private List<EventEntity> eventEntityList = new ArrayList<>();
}
