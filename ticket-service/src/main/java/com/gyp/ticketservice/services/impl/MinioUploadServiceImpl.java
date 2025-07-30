package com.gyp.ticketservice.services.impl;

import java.util.UUID;

import com.gyp.ticketservice.services.MinioUploadService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MinioUploadServiceImpl implements MinioUploadService {
	@Value("${minio.bucket}")
	private String bucket;

	private final MinioClient minioClient;

	@Override
	public String upload(MultipartFile file) {
		try {
			boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
			if(!exists) {
				minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
			}

			String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();

			minioClient.putObject(
					PutObjectArgs.builder()
							.bucket(bucket)
							.object(filename)
							.stream(file.getInputStream(), file.getSize(), -1)
							.contentType(file.getContentType())
							.build()
			);
			return filename;
		} catch(Exception e) {
			throw new RuntimeException("Error uploading file", e);
		}
	}
}
