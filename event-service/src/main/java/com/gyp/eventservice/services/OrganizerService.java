package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.common.models.UserAccountEventModel;
import com.gyp.eventservice.dtos.organizer.OrganizerResponseDto;

public interface OrganizerService {
	void syncOrganizer(List<UserAccountEventModel> modelList);

	void syncOrganizerFromAuth(String organizerId, UserAccountEventModel data);

	void handleOrganizerUpdated(String organizerId);

	void handleOrganizerDeleted(String organizerId);

	List<OrganizerResponseDto> getOutOfSyncOrganizers();

	void performFullSync();

	OrganizerResponseDto getOrganizerById(String organizerId);
}
