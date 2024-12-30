package com.example.common.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class Serialization {
	private Serialization() {
	}

	public static <T> String serializeToString(T object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		if(object == null) {
			throw new IllegalArgumentException("Object to serialize cannot be null");
		}

		return objectMapper.writeValueAsString(object);
	}

	public static <T> T deserializeFromString(String jsonString, Class<T> clazz) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		if(jsonString == null || jsonString.isEmpty()) {
			throw new IllegalArgumentException("JSON string cannot be null or empty");
		}
		if(clazz == null) {
			throw new IllegalArgumentException("Class type cannot be null");
		}
		return objectMapper.readerFor(clazz).readValue(jsonString);
	}

	public static <T> T deserializeFromString(String jsonString, TypeReference<T> valueTypeRef) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		if(jsonString == null || jsonString.isEmpty()) {
			throw new IllegalArgumentException("JSON string cannot be null or empty");
		}
		if(valueTypeRef == null) {
			throw new IllegalArgumentException("Class type cannot be null");
		}
		return objectMapper.readValue(jsonString, valueTypeRef);
	}
}
