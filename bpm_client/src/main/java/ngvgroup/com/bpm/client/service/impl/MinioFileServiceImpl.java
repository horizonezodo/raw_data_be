package ngvgroup.com.bpm.client.service.impl;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.client.dto.shared.FileDto;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.bpm.client.service.FileService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnClass(name = "io.minio.MinioClient")
@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(name = "bpm.client.minio.enabled", havingValue = "true", matchIfMissing = true)
public class MinioFileServiceImpl implements FileService {

    private final MinioClient minioClient;
    private final BpmFeignClient bpmFeignClient;

    @Value("${minio.bucket}")
    private String bucket;

    @Override
    public FileDto uploadFile(String path, MultipartFile file) {
        try {
            checkAndCreateBucket(bucket);

            String originalFilename = file.getOriginalFilename();
            // Thêm timestamp để tên file là duy nhất
            String uniqueFileName = System.currentTimeMillis() + "_" + originalFilename;

            // Xử lý logic đường dẫn
            String objectName = (path != null && !path.isEmpty())
                    ? path + "/" + uniqueFileName
                    : uniqueFileName;

            // 1. Upload lên MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            log.info("Uploaded successfully: {}", objectName);

            // 2. Trả về DTO để Frontend có đủ thông tin map vào form
            return FileDto.builder()
                    .fileName(originalFilename)
                    .filePath(objectName) // Quan trọng: FE sẽ dùng cái này để gửi lại khi Save
                    .fileSize(file.getSize())
                    .fileType(file.getContentType())
                    .build();

        } catch (Exception e) {
            log.error("Upload error: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Lỗi upload file: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadFileById(String fileId) {
        if (fileId == null || fileId.isEmpty())
            return ResponseEntity.ok(new byte[0]);

        FileDto file = bpmFeignClient.getFileDetail(fileId).getData();

        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder().bucket(bucket).object(file.getFilePath()).build())) {

            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename(file.getFileName(), StandardCharsets.UTF_8)
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(stream.readAllBytes());

        } catch (Exception e) {
            log.error("Download error for path {}: {}", file.getFilePath(), e.getMessage());
            // Trả về mảng rỗng hoặc ném lỗi tùy nghiệp vụ
            return ResponseEntity.ok(new byte[0]);
        }
    }

    @Override
    public void deleteFile(String filePath) {
        try {
            if (filePath != null && !filePath.isEmpty()) {
                minioClient.removeObject(
                        RemoveObjectArgs.builder().bucket(bucket).object(filePath).build());
                log.info("Deleted file: {}", filePath);
            }
        } catch (Exception e) {
            log.error("Delete error: {}", e.getMessage());
            // Thường thì lỗi xóa file không cần chặn luồng chính, chỉ cần log lại
        }
    }

    @Override
    public InputStream downloadFileStream(String filePath) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(filePath)
                            .build());
        } catch (Exception e) {
            log.error("Error downloading file stream: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Lỗi tải file: " + e.getMessage());
        }
    }

    private void checkAndCreateBucket(String bucketName) {
        try {
            boolean isExist = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Không tạo được bucket");
        }
    }
}
