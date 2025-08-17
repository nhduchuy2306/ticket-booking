package com.gyp.salechannelservice.entities;

import java.io.Serial;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.gyp.common.entities.AbstractEntity;
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
	@Serial
	private static final long serialVersionUID = -306600732638031531L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(name = "organization_id", nullable = false)
	private String organizationId;

	@ManyToOne
	@JoinColumn(name = "sale_channel_id", nullable = false)
	private SaleChannelEntity saleChannelEntity;

	@Column(name = "config_key")
	private String configKey;

	@Lob
	@Column(name = "config_data")
	private String configData;
}
