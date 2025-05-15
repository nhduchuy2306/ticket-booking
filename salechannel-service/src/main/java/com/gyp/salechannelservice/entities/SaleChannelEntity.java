package com.gyp.salechannelservice.entities;

import java.io.Serial;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.gyp.common.enums.salechannel.SaleChannelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "SALECHANNEL")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SaleChannelEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 3004379468671448341L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

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
}
