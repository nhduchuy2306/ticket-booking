package com.gyp.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationObjectDto {
	private String fieldName;
	private String message;
	private String type;
}
