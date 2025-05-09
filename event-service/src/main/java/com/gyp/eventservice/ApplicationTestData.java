package com.gyp.eventservice;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyp.common.tools.ApplicationTools;
import com.gyp.eventservice.dtos.seatmap.RectangleSeatMap;
import com.gyp.eventservice.dtos.seatmap.Row;
import com.gyp.eventservice.dtos.seatmap.Seat;
import com.gyp.eventservice.dtos.seatmap.SeatConfig;
import com.gyp.eventservice.dtos.seatmap.Section;
import com.gyp.eventservice.dtos.seatmap.StageConfig;
import com.gyp.eventservice.dtos.seatmap.StageOrientation;
import com.gyp.eventservice.dtos.seatmap.StageShape;

public class ApplicationTestData {

	public static Object createExampleSeatConfig() throws IOException {
		List<Seat> seats = List.of(
				new Seat("A1", 0, 0, 10.0, "VIP", 150.0, "Available"),
				new Seat("A2", 1, 0, 10.0, "VIP", 150.0, "Available"),
				new Seat("A3", 2, 0, 10.0, "VIP", 150.0, "Available"),
				new Seat("A4", 3, 0, 10.0, "VIP", 150.0, "Available"),
				new Seat("A5", 4, 0, 10.0, "VIP", 150.0, "Available")
		);

		List<Row> rows = List.of(
				new Row("A", 5, 150.0, seats)
		);

		List<Section> sections = List.of(
				new Section("Main Section", "VIP", rows)
		);

		RectangleSeatMap seatMap = new RectangleSeatMap(3, 5, sections);

		SeatConfig seatConfig = new SeatConfig();
		seatConfig.setVenueType("INDOOR");
		seatConfig.setSeatTypeColors(Map.of("Red", "#123", "Yellow", "#234"));
		seatConfig.setSeatMap(seatMap);

		StageConfig stageConfig = new StageConfig();
		stageConfig.setLabel("Artistic Center Stage");
		stageConfig.setStageX(100);
		stageConfig.setStageY(75);
		stageConfig.setStageWidth(250);
		stageConfig.setStageHeight(120);
		stageConfig.setShape(StageShape.CUSTOM);
		stageConfig.setOrientation(StageOrientation.BOTTOM);
		stageConfig.setSvgPath("M10 10 H 90 V 90 H 10 L 10 10");

		Map<String, Object> mp = new HashMap<>();
		mp.put("seatConfig", seatConfig);
		mp.put("stageConfig", stageConfig);

		return mp;
	}

	public static String minifyExampleData() throws JsonProcessingException {
		String data = """
				{
				    "venueType": "RECTANGLE",
				    "seatTypeColors": {
				      "VIP": "#FFD700",
				      "REGULAR": "#90EE90",
				      "ECONOMY": "#ADD8E6"
				    },
				    "seatMap": {
				      "@type": "RectangleSeatMap",
				      "totalRows": 3,
				      "seatsPerRow": 5,
				      "sections": [
				        {
				          "name": "Main Section",
				          "seatType": "VIP",
				          "rows": [
				            {
				              "rowName": "A",
				              "seatCount": 5,
				              "price": 150.0,
				              "seats": [
				                {
				                  "seatId": "A1",
				                  "x": 0,
				                  "y": 0,
				                  "ticketTypeId": "VIP",
				                  "price": 150.0
				                },
				                {
				                  "seatId": "A2",
				                  "x": 1,
				                  "y": 0,
				                  "ticketTypeId": "VIP",
				                  "price": 150.0
				                },
				                {
				                  "seatId": "A3",
				                  "x": 2,
				                  "y": 0,
				                  "ticketTypeId": "VIP",
				                  "price": 150.0
				                },
				                {
				                  "seatId": "A4",
				                  "x": 3,
				                  "y": 0,
				                  "ticketTypeId": "VIP",
				                  "price": 150.0
				                },
				                {
				                  "seatId": "A5",
				                  "x": 4,
				                  "y": 0,
				                  "ticketTypeId": "VIP",
				                  "price": 150.0
				                }
				              ]
				            }
				          ]
				        }
				      ]
				    }
				  }
				""";
		ObjectMapper mapper = new ObjectMapper();
		Object jsonObject = mapper.readValue(data, Object.class); // Parse JSON
		return mapper.writeValueAsString(jsonObject);
	}

	public static void main(String[] args) throws IOException {
		System.out.println(ApplicationTools.serializeData(createExampleSeatConfig()));
		System.out.println(minifyExampleData());
	}
}
