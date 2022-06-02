package com.studyforces.sourcesapi.services;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PostPolicy;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class FileService {
    private static final Logger LOG = LoggerFactory.getLogger(FileService.class);
    private final MinioClient minioClient;
    private final FileServiceConfigurationProperties configurationProperties;

    @Autowired
    public FileService(MinioClient minioClient, FileServiceConfigurationProperties configurationProperties) {
        this.minioClient = minioClient;
        this.configurationProperties = configurationProperties;
    }

    @Async
    public String objectURL(String fileName) throws Exception {
        Map<String, String> reqParams = new HashMap();
        reqParams.put("response-content-type", "application/json");

        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(configurationProperties.getBucket())
                        .object(fileName)
                        .expiry(1, TimeUnit.HOURS)
                        .extraQueryParams(reqParams)
                        .build());
    }

    @Async
    public String uploadURL(String contentType) throws Exception {
        String fileName = UUID.randomUUID().toString();

        HashMap<String, String> customHeaders = new HashMap<>();
        customHeaders.put("Content-Type", contentType);

        return minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.PUT)
                                .bucket(configurationProperties.getBucket())
                                .object(fileName)
                                .extraHeaders(customHeaders)
                                .expiry(1, TimeUnit.DAYS)
                                .build());
    }
}
