package com.gyp.salechannelservice.entities;

import java.io.Serial;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.gyp.common.enums.salechannel.SaleChannelEventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@Table(name = "SALECHANNELEVENT")
@AllArgsConstructor
@NoArgsConstructor
public class SaleChannelEventEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 1627258973752587097L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@ManyToOne
	@JoinColumn(name = "sale_channel_id", nullable = false)
	private SaleChannelEntity saleChannelEntity;

	@Column(name = "event_id", nullable = false)
	private String eventId;

	@Column(name = "start_sale_at", nullable = false)
	private LocalDateTime startSaleAt;

	@Column(name = "end_sale_at", nullable = false)
	private LocalDateTime endSaleAt;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private SaleChannelEventStatus status;
}
