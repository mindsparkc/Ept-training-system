package com.sinosoft.uploadfile.config;

import io.minio.MinioClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {
    private static String url;
    private static String accessKey;
    private static String secretKey;
    private static String defaultBucket;

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        MinioConfig.url = url;
    }

    public static String getAccessKey() {
        return accessKey;
    }

    public static void setAccessKey(String accessKey) {
        MinioConfig.accessKey = accessKey;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public static void setSecretKey(String secretKey) {
        MinioConfig.secretKey = secretKey;
    }

    public static String getDefaultBucket() {
        return defaultBucket;
    }

    public static void setDefaultBucket(String defaultBucket) {
        MinioConfig.defaultBucket = defaultBucket;
    }

    @Bean
    public MinioClient getMinioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }
}
