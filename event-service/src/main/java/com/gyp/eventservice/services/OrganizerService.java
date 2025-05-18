package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.common.models.UserAccountEventModel;

public interface OrganizerService {
	void syncOrganizer(List<UserAccountEventModel> modelList);
}
