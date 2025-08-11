package com.gyp.salechannelservice.entities;

import java.io.Serial;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SALECHANNELORDER")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleChannelOrderEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 515280665575915623L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@ManyToOne
	@JoinColumn(name = "sale_channel_id", nullable = false)
	private SaleChannelEntity saleChannel;

	@Column(name = "order_id", nullable = false)
	private String orderId;

	@Column(name = "revenue", nullable = false)
	private Double revenue;
}
