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
public class TicketOrderLinkRequestDto {
	private String ticketId;
	private String orderId;
	private LocalDateTime purchaseTime;
}

