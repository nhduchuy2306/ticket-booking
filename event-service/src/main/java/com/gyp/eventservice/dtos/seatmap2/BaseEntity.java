package com.gyp.eventservice.dtos.seatmap2;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Lớp cơ sở cho tất cả các đối tượng trong hệ thống
 */
@Getter
@Setter
@AllArgsConstructor
public abstract class BaseEntity {
	private String id;
	private String name;

	public BaseEntity() {
		this.id = UUID.randomUUID().toString();
	}
}
