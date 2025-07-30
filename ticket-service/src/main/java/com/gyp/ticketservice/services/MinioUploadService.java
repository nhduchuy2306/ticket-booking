package com.gyp.ticketservice.services;

import org.springframework.web.multipart.MultipartFile;

public interface MinioUploadService {
	String upload(MultipartFile file);
}
