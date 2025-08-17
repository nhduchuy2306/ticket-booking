package com.gyp.salechannelservice.dtos.salechannelorder;

import com.gyp.common.dtos.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleChannelOrderResponseDto extends AbstractDto {
	private String id;
	private String saleChannelId;
	private String orderId;
	private Double revenue;
}
