package com.gyp.ticketservice.dtos.tickettype;

import java.time.LocalDateTime;

import com.gyp.ticketservice.dtos.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TicketTypeResponseDto extends AbstractDto {
	private String id;
	private String eventId;
	private String name;
	private String description;
	private double price;
	private Integer quantityAvailable;
	private LocalDateTime saleStartDate;
	private LocalDateTime saleEndDate;
}
