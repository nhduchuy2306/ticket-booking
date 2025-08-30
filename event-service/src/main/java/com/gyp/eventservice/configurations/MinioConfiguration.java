package com.gyp.eventservice.configurations;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfiguration {
	@Value("${minio.url}")
	private String url;

	@Value("${minio.access-key}")
	private String accessKey;

	@Value("${minio.secret-key}")
	private String secretKey;

	@Value("${minio.bucket}")
	private String bucket;

	@Bean
	public MinioClient minioClient()
			throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
			NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
			InternalException {
		MinioClient minioClient = MinioClient.builder()
				.endpoint(url)
				.credentials(accessKey, secretKey)
				.build();

		String policy = String.format("""
				{
				    "Version": "2012-10-17",
				    "Statement": [
				        {
				            "Effect": "Allow",
				            "Principal": {
				                "AWS": ["*"]
				            },
				            "Action": [
				                "s3:GetBucketLocation",
				                "s3:ListBucket"
				            ],
				            "Resource": ["arn:aws:s3:::%s"]
				        },
				        {
				            "Effect": "Allow",
				            "Principal": {
				                "AWS": ["*"]
				            },
				            "Action": [
				                "s3:GetObject"
				            ],
				            "Resource": ["arn:aws:s3:::%s/*"]
				        }
				    ]
				}
				""", bucket, bucket);

		minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
				.bucket(bucket)
				.config(policy)
				.build());

		return minioClient;
	}
}
