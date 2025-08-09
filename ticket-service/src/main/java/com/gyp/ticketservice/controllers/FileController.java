package com.gyp.ticketservice.controllers;

import com.gyp.common.services.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(FileController.FILE_CONTROLLER_PATH)
public class FileController {
	public static final String FILE_CONTROLLER_PATH = "/files";

	private final UploadService uploadService;

	@PostMapping("/upload")
	public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
		String filename = uploadService.upload(file);
		return ResponseEntity.ok(filename);
	}
}
