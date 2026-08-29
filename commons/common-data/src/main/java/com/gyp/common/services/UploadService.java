package com.gyp.common.services;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
	Pair<String, String> upload(MultipartFile file);

	Pair<String, String> upload(byte[] file, String originalFilename, String contentType);

	String getFileUrl(String filename);

	byte[] getFileData(String filename);

	byte[] getFileDataFromSpecificBucket(String filename, String bucket);

	void deleteFile(String filename) throws Exception;
}
