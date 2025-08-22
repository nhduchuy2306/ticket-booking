package com.gyp.eventservice.controllers;

import com.gyp.common.controllers.AbstractController;
import com.gyp.eventservice.dtos.eventimage.EventImageRequestDto;
import com.gyp.eventservice.services.EventImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(EventImageController.EVENT_IMAGE_CONTROLLER_RESOURCE_PATH)
public class EventImageController extends AbstractController {
	public static final String EVENT_IMAGE_CONTROLLER_RESOURCE_PATH = "/event-images";

	private final EventImageService eventImageService;

	@GetMapping
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT_IMAGE, #ActionPerm.READ)")
	public ResponseEntity<?> getAllEventImages() {
		return ResponseEntity.ok(eventImageService.getEventImages());
	}

	@GetMapping("/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT_IMAGE, #ActionPerm.READ)")
	public ResponseEntity<?> getEventById(@PathVariable(ID_PARAM) String id) {
		return ResponseEntity.ok(eventImageService.getEventImageById(id));
	}

	@PostMapping
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT_IMAGE, #ActionPerm.CREATE)")
	public ResponseEntity<?> createEventImage(
			@RequestPart(value = "data", required = false) EventImageRequestDto dto,
			@RequestPart(value = "image", required = false) MultipartFile imageFile) {
		var createdEventImage = eventImageService.createEventImageDto(dto, imageFile);
		return ResponseEntity.ok(createdEventImage);
	}

	@PutMapping("/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT_IMAGE, #ActionPerm.UPDATE)")
	public ResponseEntity<?> updateEventImage(@PathVariable(ID_PARAM) String id,
			@RequestPart(value = "data", required = false) EventImageRequestDto dto,
			@RequestPart(value = "image", required = false) MultipartFile imageFile) {
		var updatedEventImage = eventImageService.updateEventImage(id, dto, imageFile);
		return ResponseEntity.ok(updatedEventImage);
	}

	@DeleteMapping("/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT_IMAGE, #ActionPerm.DELETE)")
	public ResponseEntity<?> deleteEventImage(@PathVariable(ID_PARAM) String id) {
		eventImageService.deleteEventImage(id);
		return ResponseEntity.noContent().build();
	}
}
