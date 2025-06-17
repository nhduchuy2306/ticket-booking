package com.gyp.eventservice.services.criteria;

import java.time.LocalDateTime;

import com.gyp.common.enums.event.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketSearchCriteria {
	private String eventId;
	private TicketStatus status;
	private Double minPrice;
	private Double maxPrice;
	private Integer minQuantity; // Minimum available quantity
	private LocalDateTime availableFrom; // Sale start date
	private LocalDateTime availableTo; // Sale end date

	// Pagination
	@Builder.Default
	private Integer page = 0;

	@Builder.Default
	private Integer size = 20;

	@Builder.Default
	private String sortBy = "price";

	@Builder.Default
	private String sortDirection = "ASC";
}
