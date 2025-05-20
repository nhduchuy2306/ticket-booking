package com.gyp.common.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class Serialization {
	private Serialization() {
	}

	public static <T> String serializeToString(T object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
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

	public static <T> T deserializeFromString(String jsonString, TypeReference<T> typeRef)
			throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		if(jsonString == null || jsonString.isEmpty()) {
			throw new IllegalArgumentException("JSON string cannot be null or empty");
		}
		return objectMapper.readValue(jsonString, typeRef);
	}

	public static <T> T deserializeFromString(String jsonString) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		if(jsonString == null || jsonString.isEmpty()) {
			throw new IllegalArgumentException("JSON string cannot be null or empty");
		}
		return objectMapper.readValue(jsonString, new TypeReference<>() {});
	}
}
