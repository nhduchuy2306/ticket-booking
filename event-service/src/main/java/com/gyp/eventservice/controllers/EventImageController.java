package com.gyp.eventservice.controllers;

import com.gyp.common.controllers.AbstractController;
import com.gyp.eventservice.dtos.eventimage.EventImageRequestDto;
import com.gyp.eventservice.services.EventImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EventImageController.EVENT_IMAGE_CONTROLLER_RESOURCE_PATH)
public class EventImageController extends AbstractController {
	public static final String EVENT_IMAGE_CONTROLLER_RESOURCE_PATH = "/event-images";

	private final EventImageService eventImageService;

	@GetMapping
	public ResponseEntity<?> getAllEventImages() {
		return ResponseEntity.ok(eventImageService.getEventImages());
	}

	@PostMapping
	public ResponseEntity<?> createEventImage(@RequestBody EventImageRequestDto dto) {
		var createdEventImage = eventImageService.createEventImageDto(dto);
		return ResponseEntity.ok(createdEventImage);
	}

	@PutMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> updateEventImage(@PathVariable(ID_PARAM) String id,
			@RequestBody EventImageRequestDto dto) {
		var updatedEventImage = eventImageService.updateEventImage(id, dto);
		return ResponseEntity.ok(updatedEventImage);
	}

	@DeleteMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> deleteEventImage(@PathVariable(ID_PARAM) String id) {
		eventImageService.deleteEventImage(id);
		return ResponseEntity.noContent().build();
	}
}
