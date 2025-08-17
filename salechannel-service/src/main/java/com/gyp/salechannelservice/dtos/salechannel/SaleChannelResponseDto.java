package com.gyp.salechannelservice.dtos.salechannel;

import java.time.LocalDateTime;

import com.gyp.common.dtos.AbstractDto;
import com.gyp.common.enums.salechannel.SaleChannelStatus;
import com.gyp.common.enums.salechannel.SaleChannelType;
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
public class SaleChannelResponseDto extends AbstractDto {
	private String id;
	private String name;
	private SaleChannelType type;
	private SaleChannelStatus status;
	private String description;
	private Double commissionRate;
	private String organizationId;
	private LocalDateTime startSaleAt;
	private LocalDateTime endSaleAt;
}
