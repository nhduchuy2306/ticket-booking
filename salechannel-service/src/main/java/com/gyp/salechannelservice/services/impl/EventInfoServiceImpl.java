package com.gyp.salechannelservice.services.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.gyp.common.models.EventEventModel;
import com.gyp.salechannelservice.entities.EventInfoEntity;
import com.gyp.salechannelservice.mappers.EventInfoMapper;
import com.gyp.salechannelservice.repositories.EventInfoRepository;
import com.gyp.salechannelservice.services.EventInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class EventInfoServiceImpl implements EventInfoService {
	private final EventInfoRepository eventInfoRepository;
	private final EventInfoMapper eventInfoMapper;

	@Override
	public void syncEvent(List<EventEventModel> modelList) {
		if(CollectionUtils.isEmpty(modelList)) {
			return;
		}

		Set<String> incomingIds = modelList.stream().map(EventEventModel::getId).collect(Collectors.toSet());
		Set<String> existingIds = eventInfoRepository.findAllById(incomingIds)
				.stream()
				.map(EventInfoEntity::getId)
				.collect(Collectors.toSet());

		List<EventEventModel> newModels = modelList.stream()
				.filter(model -> !existingIds.contains(model.getId()))
				.toList();

		if(!CollectionUtils.isEmpty(newModels)) {
			List<EventInfoEntity> eventInfoEntityList = modelList.stream()
					.map(eventInfoMapper::toEntity)
					.toList();
			eventInfoRepository.saveAll(eventInfoEntityList);
		}
	}
}
