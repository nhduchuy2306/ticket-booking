package com.gyp.salechannelservice.entities;

import java.io.Serial;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Getter
@Setter
@Builder
@Entity
@Table(name = "EVENTSALECHANNEL")
@AllArgsConstructor
@NoArgsConstructor
public class EventSaleChannelEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 6400689172046626809L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "event_id")
	private Long eventId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_channel_id")
	private SaleChannelEntity saleChannelEntity;

	@Column(name = "custom_url")
	private String customUrl;

	@Column(name = "is_published")
	private Boolean isPublished;
}
