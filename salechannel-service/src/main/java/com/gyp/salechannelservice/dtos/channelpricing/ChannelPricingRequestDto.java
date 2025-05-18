package com.gyp.salechannelservice.dtos.channelpricing;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelPricingRequestDto {
	private String channelId;
	private String ticketTypeId;
	private Double specialPrice;
	private Double markupPercentage;
	private LocalDateTime validFrom;
	private LocalDateTime validUntil;
	private Boolean isActive;
}
