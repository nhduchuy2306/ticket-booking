package com.gyp.eventservice.dtos.seatmap;

import java.util.List;

public interface HasSection {
	List<Section> getSections();

	void setSections(List<Section> sections);
}
