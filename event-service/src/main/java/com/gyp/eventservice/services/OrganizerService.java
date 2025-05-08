package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.common.models.UserAccountModel;

public interface OrganizerService {
	void syncOrganizer(List<UserAccountModel> modelList);
}
