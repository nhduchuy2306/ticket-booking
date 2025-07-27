package com.gyp.common.dtos.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedDto {
	private int page;
	private int size;

	public Pageable toPageable() {
		return PageRequest.of(page, size);
	}
}
