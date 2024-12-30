package com.gyp.common.dtos.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedDto {
	private int page;
	private int size;
	private int totalElements;
	private int totalPages;
}
