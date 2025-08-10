package com.gyp.common.services.impl;

import java.io.InputStream;
import java.util.UUID;

import com.gyp.common.services.UploadService;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {
	@Value("${minio.bucket}")
	private String bucket;

	private final MinioClient minioClient;

	@Override
	public Pair<String, String> upload(MultipartFile file) {
		try {
			boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
			if(!exists) {
				minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
			}

			String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
			String originalFilename = file.getOriginalFilename();

			minioClient.putObject(PutObjectArgs.builder()
					.bucket(bucket)
					.object(filename)
					.stream(file.getInputStream(), file.getSize(), -1)
					.contentType(file.getContentType())
					.build());
			return Pair.of(filename, originalFilename);
		} catch(Exception e) {
			throw new RuntimeException("Error uploading file", e);
		}
	}

	@Override
	public String getFileUrl(String filename) {
		try {
			return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
					.method(Method.GET)
					.bucket(bucket)
					.object(filename)
					.expiry(60 * 60)
					.build());
		} catch(Exception e) {
			throw new RuntimeException("Error getting file URL", e);
		}
	}

	@Override
	public byte[] getFileData(String filename) {
		try(InputStream stream = minioClient.getObject(
				GetObjectArgs.builder().bucket(bucket).object(filename).build())) {
			return stream.readAllBytes();
		} catch(Exception e) {
			throw new RuntimeException("Error reading file from MinIO", e);
		}
	}

	@Override
	public void deleteFile(String filename) throws Exception {
		try {
			minioClient.removeObject(RemoveObjectArgs.builder()
					.bucket(bucket)
					.object(filename)
					.build());
		} catch(Exception e) {
			throw new Exception("Error deleting file from MinIO", e);
		}
	}
}
