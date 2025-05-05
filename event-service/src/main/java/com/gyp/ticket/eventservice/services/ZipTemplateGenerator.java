package com.gyp.ticket.eventservice.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

@Service
public class ZipTemplateGenerator {
	public void createTemplateZip(File outputZipFile) throws IOException {
		try(FileOutputStream fos = new FileOutputStream(outputZipFile);
				ZipOutputStream zipOut = new ZipOutputStream(fos)) {

			// TEMPLATE.json
			String jsonContent = "";
			zipOut.putNextEntry(new ZipEntry("TEMPLATE.json"));
			zipOut.write(jsonContent.getBytes());
			zipOut.closeEntry();

			// TEMPLATE_DOC.txt
			String docContent = "";
			zipOut.putNextEntry(new ZipEntry("TEMPLATE_DOC.txt"));
			zipOut.write(docContent.getBytes());
			zipOut.closeEntry();
		}
	}
}
