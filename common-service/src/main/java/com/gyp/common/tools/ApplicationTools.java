package com.gyp.common.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ApplicationTools {
	private ApplicationTools() {
	}

	public static String minifyJson(String json) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Object jsonObject = mapper.readValue(json, Object.class);
		return mapper.writeValueAsString(jsonObject);
	}

	public static String serializeData(Object data) throws IOException {
		OutputStream out = new ByteArrayOutputStream();

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(out, data);

		return out.toString();
	}
}
