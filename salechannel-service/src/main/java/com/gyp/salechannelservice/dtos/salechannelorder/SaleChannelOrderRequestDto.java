package com.gyp.salechannelservice.dtos.salechannelorder;

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
public class SaleChannelOrderRequestDto {
	private String saleChannelId;
	private String orderId;
	private Double revenue;
}
