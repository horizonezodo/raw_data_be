package ngvgroup.com.bpm.client.dto.shared;

import org.springframework.web.multipart.MultipartFile;
import java.io.*;

public class BpmMultipartFile implements MultipartFile {

    private final byte[] input;
    private final String name;
    private final String originalFilename;
    private final String contentType;

    public BpmMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.input = content;
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getOriginalFilename() { return originalFilename; }

    @Override
    public String getContentType() { return contentType; }

    @Override
    public boolean isEmpty() { return input == null || input.length == 0; }

    @Override
    public long getSize() { return input.length; }

    @Override
    public byte[] getBytes() throws IOException { return input; }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(input);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(input);
        }
    }
}
