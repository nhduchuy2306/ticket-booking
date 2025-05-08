package com.gyp.eventservice.controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.gyp.eventservice.services.DirectoryService;
import com.gyp.eventservice.services.SeatMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(SeatMapController.SEAT_MAP_CONTROLLER_RESOURCE_PATH)
public class SeatMapController {
	public static final String SEAT_MAP_CONTROLLER_RESOURCE_PATH = "/seatmaps";

	private static final String UPLOAD_PATH = "/upload";
	private static final String DOWNLOAD_TEMPLATE_PATH = "/downloadtemplate";

	private static final String FILE_PARAM = "fileName";

	private final SeatMapService seatMapService;
	private final DirectoryService directoryService;

	@PostMapping(value = UPLOAD_PATH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
}
