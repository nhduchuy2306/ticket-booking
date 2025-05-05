package com.gyp.ticket.eventservice.services.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.gyp.common.models.UserAccountModel;
import com.gyp.ticket.eventservice.entities.OrganizerEntity;
import com.gyp.ticket.eventservice.mappers.OrganizerMapper;
import com.gyp.ticket.eventservice.repositories.OrganizerRepository;
import com.gyp.ticket.eventservice.services.OrganizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class OrganizerServiceImpl implements OrganizerService {
	private final OrganizerRepository organizerRepository;

	private final OrganizerMapper organizerMapper;

	@Override
	public void syncOrganizer(List<UserAccountModel> modelList) {
		if(CollectionUtils.isEmpty(modelList)) {
			return;
		}

		Set<String> incomingIds = modelList.stream().map(UserAccountModel::getId).collect(Collectors.toSet());
		Set<String> existingIds = organizerRepository.findAllById(incomingIds)
				.stream()
				.map(OrganizerEntity::getId)
				.collect(Collectors.toSet());

		List<UserAccountModel> newModels = modelList.stream()
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
