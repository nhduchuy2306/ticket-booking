package com.gyp.common.validators.criteria;

import java.util.List;

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
public class ValidationInfo {
	private String className;
	private List<ValidationDetail> details;

	public static ValidationInfo empty() {
		return ValidationInfo.builder()
				.className("")
				.details(List.of())
				.build();
	}
}
