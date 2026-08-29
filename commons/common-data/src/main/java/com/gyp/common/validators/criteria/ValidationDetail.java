package com.gyp.common.validators.criteria;

import java.util.HashMap;
import java.util.Map;

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
public class ValidationDetail {
	private String name;
	private String fieldName;
	private String errorMessage;
	private Object defaultValue;
	private Map<String, Object> attributes;

	public ValidationDetail(String name, String fieldName, String errorMessage) {
		this.name = name;
		this.fieldName = fieldName;
		this.errorMessage = errorMessage;
	}

	public void addAttribute(String key, Object value) {
		if(attributes == null) {
			attributes = new HashMap<>();
		}
		attributes.put(key, value);
	}
}
