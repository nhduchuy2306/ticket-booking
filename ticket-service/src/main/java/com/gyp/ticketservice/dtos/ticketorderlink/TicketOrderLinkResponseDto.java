package com.gyp.ticketservice.dtos.ticketorderlink;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketOrderLinkResponseDto {
	private String id;
	private String ticketId;
	private String orderId;
	private LocalDateTime purchaseTime;
	private LocalDateTime createTimestamp;
	private LocalDateTime changeTimestamp;
}

