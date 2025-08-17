package com.gyp.salechannelservice.dtos.salechannelevent;

import com.gyp.common.dtos.AbstractDto;
import com.gyp.salechannelservice.dtos.salechannel.SaleChannelResponseDto;
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
public class SaleChannelEventResponseDto extends AbstractDto {
	private String id;
	private SaleChannelResponseDto saleChannel;
	private String eventId;
}
