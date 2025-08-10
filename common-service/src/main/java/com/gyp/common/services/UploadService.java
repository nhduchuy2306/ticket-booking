package com.gyp.common.services;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
	Pair<String, String> upload(MultipartFile file);

	String getFileUrl(String filename);

	byte[] getFileData(String filename);

	void deleteFile(String filename) throws Exception;
}
