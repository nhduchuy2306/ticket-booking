package com.gyp.common.tools;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMinifier {
	public static String minifyJson(String json) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Object jsonObject = mapper.readValue(json, Object.class); // Parse JSON
		return mapper.writeValueAsString(jsonObject); // Write as compact JSON
	}

	public static void main(String[] args) throws Exception {
		String json = """
				{
				    "label": "Artistic Center Stage",
				    "stageX": 100,
				    "stageY": 75,
				    "stageWidth": 250,
				    "stageHeight": 120,
				    "shape": "CUSTOM",
				    "orientation": "DOWN",
				    "svgPath": "M10 10 H 90 V 90 H 10 L 10 10"
				}
				""";
		String minified = minifyJson(json);
		System.out.println(minified);
	}
}
