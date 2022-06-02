package com.studyforces.sourcesapi.services;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
@ConditionalOnClass(MinioClient.class)
@EnableConfigurationProperties(FileServiceConfigurationProperties.class)
@ComponentScan("com.studyforces.sourcesapi.services")
public class FileServiceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceConfiguration.class);

    @Autowired
    private FileServiceConfigurationProperties configurationProperties;

    @Bean
    public MinioClient minioClient() throws IOException, InvalidKeyException, NoSuchAlgorithmException, FileServiceException, MinioException {

        MinioClient minioClient = MinioClient.builder()
                .endpoint(configurationProperties.getUrl())
                .credentials(configurationProperties.getAccessKey(), configurationProperties.getSecretKey())
                .build();
        minioClient.setTimeout(
                configurationProperties.getConnectTimeout().toMillis(),
                configurationProperties.getWriteTimeout().toMillis(),
                configurationProperties.getReadTimeout().toMillis()
        );

        if (configurationProperties.isCheckBucket()) {
            try {
                LOGGER.debug("Checking if bucket {} exists", configurationProperties.getBucket());
                BucketExistsArgs existsArgs = BucketExistsArgs.builder()
                        .bucket(configurationProperties.getBucket())
                        .build();
                boolean b = minioClient.bucketExists(existsArgs);
                if (!b) {
                    if (configurationProperties.isCreateBucket()) {
                        try {
                            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                                    .bucket(configurationProperties.getBucket())
                                    .build();
                            minioClient.makeBucket(makeBucketArgs);
                        } catch (Exception e) {
                            throw new FileServiceException("Cannot create bucket", e);
                        }
                    } else {
                        throw new IllegalStateException("Bucket does not exist: " + configurationProperties.getBucket());
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error while checking bucket", e);
                throw e;
            }
        }

        return minioClient;
    }

}