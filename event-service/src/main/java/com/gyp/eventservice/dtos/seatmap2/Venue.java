package com.gyp.eventservice.dtos.seatmap2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Lớp đại diện cho một địa điểm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Venue extends BaseEntity {
	private String address;
	private int capacity;
	private String venueMapId;
}
