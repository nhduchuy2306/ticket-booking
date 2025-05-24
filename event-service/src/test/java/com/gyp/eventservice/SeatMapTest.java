package com.gyp.eventservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyp.eventservice.dtos.seatmap.Position;
import com.gyp.eventservice.dtos.seatmap.SeatLayoutFactory;
import com.gyp.eventservice.dtos.seatmap.Section;
import com.gyp.eventservice.dtos.seatmap.StageConfig;
import com.gyp.eventservice.dtos.seatmap.StageFactory;
import com.gyp.eventservice.dtos.seatmap.StageOrientation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SeatMapTest {
	@Test
	public void testCreateSection() throws JsonProcessingException {
		Section section = SeatLayoutFactory.createLinearSection(
				"Section 1",
				Position.builder().x(10).y(10).build(),
				5,
				3,
				5,
				5
		);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(section);
		System.out.println(json);
		Assertions.assertNotNull(json);
	}

	@Test
	public void testCreateStage() throws JsonProcessingException {
		StageConfig stageConfig = StageFactory.createSemicircleStage(
				"Stage1",
				Position.builder().x(10).y(10).build(),
				10,
				StageOrientation.NORTH
		);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(stageConfig);
		System.out.println(json);
		Assertions.assertNotNull(json);
	}

	@Test
	public void testCreateSeatMap() throws JsonProcessingException {
		Section section1 = SeatLayoutFactory.createLinearSection(
				"Section 1",
				Position.builder().x(10).y(10).build(),
				3,
				3,
				5,
				5
		);

		Section section2 = SeatLayoutFactory.createLinearSection(
				"Section 1",
				Position.builder().x(10).y(15).build(),
				3,
				3,
				5,
				5
		);
	}
 }
