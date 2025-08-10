package com.gyp.eventservice.services.impl;

import java.util.List;

import com.gyp.common.services.UploadService;
import com.gyp.eventservice.dtos.eventimage.EventImageRequestDto;
import com.gyp.eventservice.dtos.eventimage.EventImageResponseDto;
import com.gyp.eventservice.entities.EventImageEntity;
import com.gyp.eventservice.mappers.EventImageMapper;
import com.gyp.eventservice.repositories.EventImageRepository;
import com.gyp.eventservice.services.EventImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EventImageServiceImpl implements EventImageService {
	private final EventImageRepository eventImageRepository;
	private final EventImageMapper eventImageMapper;
	private final UploadService uploadService;

	@Override
	public List<EventImageResponseDto> getEventImages() {
		var eventImages = eventImageRepository.findAll();
		return eventImageMapper.toResponseDtoList(eventImages);
	}

	@Override
	public EventImageResponseDto createEventImageDto(EventImageRequestDto dto, MultipartFile imageFile) {
		EventImageEntity eventImageEntity = eventImageMapper.toEntity(dto);

		if(imageFile != null && !imageFile.isEmpty()) {
			// upload the image and set the URL
			var uploadResult = uploadService.upload(imageFile);
			eventImageEntity.setImageUrl(uploadResult.getLeft());
		} else {
			eventImageEntity.setImageUrl(null);
		}

		eventImageEntity = eventImageRepository.save(eventImageEntity);
		return eventImageMapper.toResponseDto(eventImageEntity);
	}

	@Override
	public EventImageResponseDto getEventImageById(String id) {
		var eventImage = eventImageRepository.findById(id)
				.map(eventImageMapper::toResponseDto)
				.orElse(null);
		if(eventImage == null) {
			throw new IllegalArgumentException("Event image with id " + id + " does not exist.");
		}
		var imageUrl = eventImage.getImageUrl();
		if(imageUrl != null) {
			eventImage.setImageUrl(uploadService.getFileUrl(imageUrl));
		}
		return eventImage;
	}

	@Override
	public EventImageResponseDto updateEventImage(String id, EventImageRequestDto dto, MultipartFile imageFile) {
		try {
			var existingEvent = eventImageRepository.findById(id);
			if(existingEvent.isEmpty()) {
				throw new IllegalArgumentException("Event image with id " + id + " does not exist.");
			}
			EventImageEntity eventImageEntity = existingEvent.get();
			eventImageMapper.updateEntityFromDto(dto, eventImageEntity);

			if(imageFile != null && !imageFile.isEmpty()) {
				// delete the old image if it exists
				if(eventImageEntity.getImageUrl() != null) {
					uploadService.deleteFile(eventImageEntity.getImageUrl());
				}

				var uploadResult = uploadService.upload(imageFile);
				eventImageEntity.setImageUrl(uploadResult.getLeft());
			}

			eventImageEntity = eventImageRepository.save(eventImageEntity);
			return eventImageMapper.toResponseDto(eventImageEntity);
		} catch(Exception e) {
			throw new RuntimeException("Error updating event image with id " + id, e);
		}
	}

	@Override
	public void deleteEventImage(String id) {
		var existingEvent = eventImageRepository.findById(id);
		if(existingEvent.isEmpty()) {
			throw new IllegalArgumentException("Event image with id " + id + " does not exist.");
		}
		existingEvent.ifPresent(eventImageRepository::delete);
	}
}
