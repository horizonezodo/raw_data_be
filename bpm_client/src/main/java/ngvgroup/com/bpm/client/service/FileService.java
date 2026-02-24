package ngvgroup.com.bpm.client.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ngvgroup.com.bpm.client.dto.shared.FileDto;
import java.io.InputStream;

public interface FileService {

    FileDto uploadFile(String path, MultipartFile file);

    ResponseEntity<byte[]> downloadFileById(String fileId);

    void deleteFile(String filePath);

    InputStream downloadFileStream(String filePath);
}
