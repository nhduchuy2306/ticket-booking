package com.gyp.common.converters;

import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;

public final class Serialization {
	private static final ObjectMapper objectMapper = new ObjectMapper()
			.registerModule(new JavaTimeModule());

	private Serialization() {
	}

	public static <T> String serializeToString(T object) throws JsonProcessingException {
		Objects.requireNonNull(object, "Object to serialize cannot be null");
		return objectMapper.writeValueAsString(object);
	}

	public static <T> T deserializeFromString(String jsonString, Class<T> clazz) throws JsonProcessingException {
		validateJsonString(jsonString);
		Objects.requireNonNull(clazz, "Class type cannot be null");
		return objectMapper.readerFor(clazz).readValue(jsonString);
	}

	public static <T> T deserializeFromString(String jsonString, TypeReference<T> typeRef)
			throws JsonProcessingException {
		validateJsonString(jsonString);
		Objects.requireNonNull(typeRef, "TypeReference cannot be null");
		return objectMapper.readValue(jsonString, typeRef);
	}

	public static <T> T deserializeFromString(String jsonString) throws JsonProcessingException {
		validateJsonString(jsonString);
		return objectMapper.readValue(jsonString, new TypeReference<>() {});
	}

	private static void validateJsonString(String json) {
		if(StringUtils.isEmpty(json)) {
			throw new IllegalArgumentException("JSON string cannot be null or empty");
		}
	}
}
