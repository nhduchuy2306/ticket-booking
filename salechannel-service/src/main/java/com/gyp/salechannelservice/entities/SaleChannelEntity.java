package com.gyp.salechannelservice.entities;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import com.gyp.common.enums.salechannel.SaleChannelType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "SALECHANNEL")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SaleChannelEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 3004379468671448341L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "channel_name")
	private String channelName;

	@Column(name = "channel_type")
	@Enumerated(EnumType.STRING)
	private SaleChannelType channelType;

	@Column(name = "description")
	private String description;

	@Column(name = "commission_rate")
	private Double commissionRate;

	@Column(name = "is_active")
	private Boolean isActive;

	@ManyToMany
	@JoinTable(
			name = "channel_event",
			joinColumns = @JoinColumn(name = "channel_id"),
			inverseJoinColumns = @JoinColumn(name = "event_id")
	)
	private List<EventInfoEntity> eventInfoEntityList = new ArrayList<>();
}
