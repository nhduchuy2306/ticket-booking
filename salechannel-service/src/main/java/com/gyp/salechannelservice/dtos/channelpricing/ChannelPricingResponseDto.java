package com.gyp.salechannelservice.dtos.channelpricing;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChannelPricingResponseDto {
	private String id;
	private String channelId;
	private String ticketTypeId;
	private Double specialPrice;
	private Double markupPercentage;
	private LocalDateTime validFrom;
	private LocalDateTime validUntil;
	private Boolean isActive;
}
