package com.gyp.orderservice.dtos.orderdetail;

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
public class OrderDetailResponseDto extends AbstractDto {
	private String id;
	private String seatId;
	private Integer quantity;
	private Double price;
	private String orderId;
}
