package com.gyp.eventservice.services;

import org.springframework.core.io.Resource;

public interface DirectoryService {
	Resource getFileByFileName(String fileName);
}
