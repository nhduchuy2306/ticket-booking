package com.gyp.eventservice.controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.gyp.common.controllers.AbstractController;
import com.gyp.eventservice.dtos.seatmap.SeatMapRequestDto;
import com.gyp.eventservice.services.DirectoryService;
import com.gyp.eventservice.services.SeatMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(SeatMapController.SEAT_MAP_CONTROLLER_RESOURCE_PATH)
public class SeatMapController extends AbstractController {
	public static final String SEAT_MAP_CONTROLLER_RESOURCE_PATH = "/seat-maps";

	private static final String UPLOAD_PATH = "/upload";
	private static final String DOWNLOAD_TEMPLATE_PATH = "/downloadtemplate";
	private static final String GENERATE_SEAT_MAP_TICKET_PATH = "/generateseatmapticket";

	private static final String FILE_PARAM = "fileName";
	private static final String EVENT_ID_PARAM = "eventId";

	private final SeatMapService seatMapService;
	private final DirectoryService directoryService;

	@GetMapping
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.SEAT_MAP, #ActionPerm.READ)")
	public ResponseEntity<?> getAllSeatMaps() {
		try {
			return ResponseEntity.ok(seatMapService.getAllSeatMaps());
		} catch(Exception e) {
			return ResponseEntity.internalServerError().body("Error fetching seat maps: " + e.getMessage());
		}
	}

	@GetMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> getSeatMapById(@PathVariable(ID_PARAM) String seatMapId) {
		try {
			return ResponseEntity.ok(seatMapService.getSeatMapById(seatMapId));
		} catch(Exception e) {
			return ResponseEntity.status(404).body("Seat map not found: " + e.getMessage());
		}
	}

	@PostMapping
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.SEAT_MAP, #ActionPerm.CREATE)")
	public ResponseEntity<?> createSeatMap(@RequestBody SeatMapRequestDto seatMapDto) {
		try {
			return ResponseEntity.ok(seatMapService.createSeatMap(seatMapDto));
		} catch(Exception e) {
			return ResponseEntity.status(400).body("Error creating seat map: " + e.getMessage());
		}
	}

	@PutMapping("/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.SEAT_MAP, #ActionPerm.UPDATE)")
	public ResponseEntity<?> updateSeatMap(@PathVariable(ID_PARAM) String seatMapId,
			@RequestBody SeatMapRequestDto seatMapDto) {
		try {
			return ResponseEntity.ok(seatMapService.updateSeatMap(seatMapId, seatMapDto));
		} catch(Exception e) {
			return ResponseEntity.status(404).body("Error updating seat map: " + e.getMessage());
		}
	}

	@DeleteMapping("/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.SEAT_MAP, #ActionPerm.DELETE)")
	public ResponseEntity<?> deleteSeatMap(@PathVariable(ID_PARAM) String seatMapId) {
		try {
			seatMapService.deleteSeatMap(seatMapId);
			return ResponseEntity.ok("Seat map deleted successfully");
		} catch(Exception e) {
			return ResponseEntity.status(404).body("Error deleting seat map: " + e.getMessage());
		}
	}

	@PostMapping(value = UPLOAD_PATH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.SEAT_MAP, #ActionPerm.IMPORT)")
	public ResponseEntity<String> uploadSeatMap(@RequestParam("file") MultipartFile file) {
		try {
			String content = new String(file.getBytes(), StandardCharsets.UTF_8);
			String convertedJson = seatMapService.convertOrganizerJson(content);
			return ResponseEntity.ok(convertedJson);
		} catch(IOException e) {
			return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
		}
	}

	@GetMapping(DOWNLOAD_TEMPLATE_PATH + "/{" + FILE_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.SEAT_MAP, #ActionPerm.EXPORT)")
	public ResponseEntity<Resource> downloadTemplate(@PathVariable(FILE_PARAM) String fileName) {
		try {
			Resource resource = directoryService.getFileByFileName(fileName);
			if(Objects.isNull(resource)) {
				return ResponseEntity.notFound().build();
			}

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

			return ResponseEntity.ok()
					.headers(headers)
					.contentType(MediaType.APPLICATION_JSON)
					.body(resource);
		} catch(Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping(GENERATE_SEAT_MAP_TICKET_PATH + "/{" + EVENT_ID_PARAM + "}")
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.TICKET_GENERATION, #ActionPerm.GENERATE)")
	public ResponseEntity<?> generateSeatMapTicket(@PathVariable(EVENT_ID_PARAM) String eventId) {
		try {
			seatMapService.generateSeatMapTicket(eventId);
			return ResponseEntity.ok("Seat map ticket generation initiated for event ID: " + eventId);
		} catch(Exception e) {
			return ResponseEntity.status(500).body("Error generating seat map ticket: " + e.getMessage());
		}
	}
}
