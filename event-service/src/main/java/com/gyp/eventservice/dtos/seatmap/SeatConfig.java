package com.gyp.eventservice.dtos.seatmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SeatConfig {
	private Map<String, String> seatTypeColors = new HashMap<>();
	private List<Section> sections = new ArrayList<>();

	public void addSection(Section section) {
		sections.add(section);
	}

	public void removeSection(Section section) {
		sections.remove(section);
	}
}
