package ngvgroup.com.bpm.client.service.impl;

import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.client.dto.shared.FileDto;
import ngvgroup.com.bpm.client.service.FileService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@Slf4j
@ConditionalOnProperty(name = "bpm.client.minio.enabled", havingValue = "false", matchIfMissing = false)
public class NoOpFileServiceImpl implements FileService {

    @Override
    public FileDto uploadFile(String path, MultipartFile file) {
        log.warn("MinIO is disabled. File upload ignored: {}", file != null ? file.getOriginalFilename() : "null");
        return null; // Return null or empty DTO dependent on caller null-safety
    }

    @Override
    public ResponseEntity<byte[]> downloadFileById(String fileId) {
        log.warn("MinIO is disabled. File download ignored for ID: {}", fileId);
        return ResponseEntity.ok(new byte[0]);
    }

    @Override
    public void deleteFile(String filePath) {
        log.warn("MinIO is disabled. File deletion ignored: {}", filePath);
    }

    @Override
    public InputStream downloadFileStream(String filePath) {
        log.error("MinIO is disabled. Cannot download file stream: {}", filePath);
        throw new UnsupportedOperationException("Chức năng lưu trữ file (MinIO) chưa được bật.");
    }

}
