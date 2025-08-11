package com.gyp.salechannelservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SALECHANNELCONFIG")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleChannelConfigEntity extends AbstractEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@ManyToOne
	@JoinColumn(name = "sale_channel_id", nullable = false)
	private SaleChannelEntity saleChannelEntity;

	@Column(name = "config_key")
	private String configKey;

	@Lob
	@Column(name = "config_data")
	private String configData;
}
