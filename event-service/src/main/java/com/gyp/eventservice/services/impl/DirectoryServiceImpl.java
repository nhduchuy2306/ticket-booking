package com.gyp.eventservice.services.impl;

import com.gyp.eventservice.services.DirectoryService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class DirectoryServiceImpl implements DirectoryService {

	private static final String TEMPLATE_PATH = "/templates";

	@Override
	public Resource getFileByFileName(String fileName) {
		Resource resource = new ClassPathResource(TEMPLATE_PATH + "/" + fileName);
		return !resource.exists() ? null : resource;
	}
}
