package com.naas.admin_service.core.config;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MinioConfig {
    @Value("${minio.url}")
    private String url;
    @Value("${minio.access-key}")
    private String accessKey;
    @Value("${minio.secret-key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        log.info("Initializing MinIO client with endpoint: {}", url);

        MinioClient.Builder builder = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey);

        // Nếu sử dụng HTTPS, có thể cần thêm cấu hình SSL
        if (url.startsWith("https://")) {
            log.info("Using HTTPS endpoint, configuring SSL...");
            // Có thể thêm cấu hình SSL nếu cần
        }

        MinioClient client = builder.build();
        log.info("MinIO client initialized successfully");
        return client;
    }
}
