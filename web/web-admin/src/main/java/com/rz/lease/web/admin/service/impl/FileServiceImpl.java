package com.rz.lease.web.admin.service.impl;

import com.rz.lease.common.minio.MinioProperties;
import com.rz.lease.web.admin.service.FileService;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.SetBucketPolicyArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author liubo
 * @description Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class FileServiceImpl implements FileService {

    private MinioClient minioClient;
    private MinioProperties minioProperties;

    public FileServiceImpl(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;

    }

    @Override
    public String upload(MultipartFile file) throws InvalidKeyException, ErrorResponseException,
            InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException,
            ServerException, XmlParserException, IllegalArgumentException, IOException {
        String url = null;

        boolean found = minioClient
                .bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
            minioClient
                    .setBucketPolicy(SetBucketPolicyArgs.builder().bucket(minioProperties.getBucketName())
                            .config(createBucketPolicyConfig(minioProperties.getBucketName())).build());
        }
        String filename = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/"
                + UUID.randomUUID() + "-" + file.getOriginalFilename();
        minioClient.putObject(PutObjectArgs.builder().bucket(minioProperties.getBucketName())
                .stream(file.getInputStream(), file.getSize(), -1)
                .object(filename).contentType(file.getContentType()).build());
        url = String.join("/", minioProperties.getEndpoint(), minioProperties.getBucketName(), filename);

        return url;
    }

    private String createBucketPolicyConfig(String bucketName) {
        return """
                {
                  "Version": "2012-10-17",
                  "Statement": [
                    {
                        "Action": ["s3:GetObject"],
                      "Effect": "Allow",
                      "Principal": "*",
                        "Resource": [ "arn:aws:s3:::%s/*" ]
                    }]
                }
                """.formatted(bucketName);
    }
}
