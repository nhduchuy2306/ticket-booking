package com.gyp.eventservice.dtos.seatmap;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp cơ sở cho tất cả các đối tượng trong hệ thống
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseSeatMap {
	private String id = UUID.randomUUID().toString();
	private String name;
}
