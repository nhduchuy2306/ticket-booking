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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CHANNELPRICING")
@NoArgsConstructor
@AllArgsConstructor
public class ChannelPricingEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 8804704960869417338L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "channel_id", nullable = false)
	private String channelId;

	@Column(name = "ticket_type_id", nullable = false)
	private String ticketTypeId;

	@Column(name = "special_price")
	private Double specialPrice;

	@Column(name = "markup_percentage")
	private Double markupPercentage;

	@Column(name = "valid_from")
	private LocalDateTime validFrom;

	@Column(name = "valid_until")
	private LocalDateTime validUntil;

	@Column(name = "is_active")
	private Boolean isActive;
}
