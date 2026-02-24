package ngvgroup.com.bpm.client.config; // Đổi package cho đúng với cấu trúc dự án của bạn

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(name = "io.minio.MinioClient")
@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(name = "bpm.client.minio.enabled", havingValue = "true", matchIfMissing = true)
public class MinIOConfig {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        // Khởi tạo Minio Client với các thông số từ file properties
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }
}
