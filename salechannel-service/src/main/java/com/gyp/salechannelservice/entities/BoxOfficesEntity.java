package com.gyp.salechannelservice.entities;

import java.io.Serial;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "BOXOFFICES")
@NoArgsConstructor
@AllArgsConstructor
public class BoxOfficesEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 7274240468414096444L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(name = "location_name")
	private String locationName;

	@Column(name = "location_address")
	private String locationAddress;

	@Column(name = "opening_hours")
	private String openingHours;

	@OneToOne
	@JoinColumn(name = "sale_channel_id", nullable = false)
	private SaleChannelEntity saleChannelEntity;
}
