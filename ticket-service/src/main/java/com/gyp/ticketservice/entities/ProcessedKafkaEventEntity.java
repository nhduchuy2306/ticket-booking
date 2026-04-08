package com.gyp.ticketservice.entities;

import java.io.Serial;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "processed_kafka_event", uniqueConstraints = {
		@jakarta.persistence.UniqueConstraint(name = "uk_processed_event_consumer_key",
				columnNames = {"consumer_name", "event_key"})
})
public class ProcessedKafkaEventEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 6678452141648525311L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "consumer_name", nullable = false)
	private String consumerName;

	@Column(name = "event_key", nullable = false)
	private String eventKey;

	@Column(name = "processed_at", nullable = false)
	private LocalDateTime processedAt;
}