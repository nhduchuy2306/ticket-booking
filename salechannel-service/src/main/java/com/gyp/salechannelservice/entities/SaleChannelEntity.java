package com.gyp.salechannelservice.entities;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gyp.common.enums.salechannel.SaleChannelStatus;
import com.gyp.common.enums.salechannel.SaleChannelType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SALECHANNEL")
@AllArgsConstructor
@NoArgsConstructor
public class SaleChannelEntity extends OrganizationScopedEntity {
	@Serial
	private static final long serialVersionUID = 3004379468671448341L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "channel_name")
	private String name;

	@Column(name = "channel_type")
	@Enumerated(EnumType.STRING)
	private SaleChannelType type;

	@Column(name = "description")
	private String description;

	@Column(name = "commission_rate")
	private Double commissionRate;

	@Lob
	@Column(name = "sale_channel_config", columnDefinition = "text")
	private String saleChannelConfig;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private SaleChannelStatus status;


	@Column(name = "organization_slug")
	private String organizationSlug;

	@Column(name = "start_sale_at", nullable = false)
	private LocalDateTime startSaleAt;

	@Column(name = "end_sale_at", nullable = false)
	private LocalDateTime endSaleAt;

	@JsonIgnore
	@OneToMany(mappedBy = "saleChannelEntity", fetch = FetchType.LAZY)
	private List<SaleChannelEventEntity> saleChannelEventEntityList = new ArrayList<>();
}
