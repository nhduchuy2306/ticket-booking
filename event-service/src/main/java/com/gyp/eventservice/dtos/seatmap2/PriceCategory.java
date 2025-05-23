package com.gyp.eventservice.dtos.seatmap2;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp đại diện cho một loại giá
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PriceCategory extends BaseEntity {
	private String color;
	private double price;
	private String eventId;
	private List<String> applicableSectionIds;
}

