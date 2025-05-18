package com.gyp.salechannelservice.entities;

import java.io.Serial;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// For tracking sales from different channels
@Entity
@Table(name = "CHANELSALES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChannelSalesEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 6546474765105671500L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "channel_id", nullable = false)
	private String channelId;

	@Column(name = "event_id", nullable = false)
	private String eventId;

	@Column(name = "total_tickets_sold")
	private int totalTicketsSold;

	@Column(name = "total_revenue")
	private Double totalRevenue;

	@Column(name = "commission_earned")
	private Double commissionEarned;

	@Column(name = "sale_date")
	private LocalDateTime saleDate;
}
