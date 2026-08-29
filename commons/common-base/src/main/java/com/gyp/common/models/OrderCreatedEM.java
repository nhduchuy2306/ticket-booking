package com.gyp.common.models;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class OrderCreatedEM {
	private String orderId;
	private String eventId;
	private String customerEmail;
	private Double totalAmount;
	private List<OrderItem> items;

	@JsonIgnore
	public List<String> getSeatIds() {
		return items.stream()
				.map(OrderItem::getSeatId)
				.collect(Collectors.toList());
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OrderItem {
		private String seatId;
		private Integer quantity;
		private Double price;
		private String orderId;
	}
}
