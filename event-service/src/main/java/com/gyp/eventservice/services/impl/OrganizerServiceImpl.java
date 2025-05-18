package com.gyp.eventservice.services.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.gyp.common.models.UserAccountEventModel;
import com.gyp.eventservice.entities.OrganizerEntity;
import com.gyp.eventservice.mappers.OrganizerMapper;
import com.gyp.eventservice.repositories.OrganizerRepository;
import com.gyp.eventservice.services.OrganizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class OrganizerServiceImpl implements OrganizerService {
	private final OrganizerRepository organizerRepository;

	private final OrganizerMapper organizerMapper;

	@Override
	public void syncOrganizer(List<UserAccountEventModel> modelList) {
		if(CollectionUtils.isEmpty(modelList)) {
			return;
		}

		Set<String> incomingIds = modelList.stream().map(UserAccountEventModel::getId).collect(Collectors.toSet());
		Set<String> existingIds = organizerRepository.findAllById(incomingIds)
				.stream()
				.map(OrganizerEntity::getId)
				.collect(Collectors.toSet());

		List<UserAccountEventModel> newModels = modelList.stream()
				.filter(model -> !existingIds.contains(model.getId()))
				.toList();

		if(!CollectionUtils.isEmpty(newModels)) {
			List<OrganizerEntity> organizerEntityList = modelList.stream()
					.map(organizerMapper::toEntity)
					.toList();
			organizerRepository.saveAll(organizerEntityList);
		}
	}
}
