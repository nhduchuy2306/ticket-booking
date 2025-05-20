package com.gyp.salechannelservice.services;

import java.util.List;

import com.gyp.common.models.EventEventModel;

public interface EventInfoService {
	void syncEvent(List<EventEventModel> modelList);
}
