package com.gyp.common.services;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
	String upload(MultipartFile file);

	String getFileUrl(String filename);

	byte[] getFileData(String filename);

	void deleteFile(String filename) throws Exception;
}
