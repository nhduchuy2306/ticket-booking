package com.gyp.ticket.eventservice.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class EventTimeEmbeddable {
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocalDateTime doorOpenTime;
	private LocalDateTime doorCloseTime;
}
