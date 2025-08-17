package com.gyp.salechannelservice.dtos.salechannelevent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleChannelEventRequestDto {
	private String saleChannelId;
	private String eventId;
}
