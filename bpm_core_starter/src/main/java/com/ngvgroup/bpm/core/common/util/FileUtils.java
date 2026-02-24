package com.ngvgroup.bpm.core.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Tiện ích xử lý file
 */
public class FileUtils {

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp");
    private static final List<String> DOCUMENT_EXTENSIONS = Arrays.asList("doc", "docx", "xls", "xlsx", "pdf", "txt", "rtf");
    private static final List<String> VIDEO_EXTENSIONS = Arrays.asList("mp4", "avi", "mov", "wmv", "flv", "mkv");
    private static final List<String> AUDIO_EXTENSIONS = Arrays.asList("mp3", "wav", "ogg", "m4a", "flac");
    private static final List<String> ARCHIVE_EXTENSIONS = Arrays.asList("zip", "rar", "7z", "tar", "gz");

    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Lấy phần mở rộng của file
     */
    public static String getExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }

    /**
     * Lấy tên file không có phần mở rộng
     */
    public static String getFileNameWithoutExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fileName;
        }
        return fileName.substring(0, lastDotIndex);
    }

    /**
     * Lấy MIME type của file
     */
    public static String getMimeType(String fileName) throws IOException {
        if (fileName == null) {
            return null;
        }
        Path path = Paths.get(fileName);
        return Files.probeContentType(path);
    }

    /**
     * Kiểm tra file có phải là ảnh không
     */
    public static boolean isImage(String fileName) {
        return IMAGE_EXTENSIONS.contains(getExtension(fileName));
    }

    /**
     * Kiểm tra file có phải là tài liệu không
     */
    public static boolean isDocument(String fileName) {
        return DOCUMENT_EXTENSIONS.contains(getExtension(fileName));
    }

    /**
     * Kiểm tra file có phải là video không
     */
    public static boolean isVideo(String fileName) {
        return VIDEO_EXTENSIONS.contains(getExtension(fileName));
    }

    /**
     * Kiểm tra file có phải là audio không
     */
    public static boolean isAudio(String fileName) {
        return AUDIO_EXTENSIONS.contains(getExtension(fileName));
    }

    /**
     * Kiểm tra file có phải là file nén không
     */
    public static boolean isArchive(String fileName) {
        return ARCHIVE_EXTENSIONS.contains(getExtension(fileName));
    }

    /**
     * Tạo thư mục nếu chưa tồn tại
     */
    public static void createDirectoryIfNotExists(String directoryPath) throws IOException {
        if (directoryPath == null) {
            return;
        }
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    /**
     * Xóa file
     */
    public static void deleteFile(String filePath) throws IOException {
        if (filePath == null) {
            return;
        }
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
    }

    /**
     * Xóa thư mục và tất cả nội dung bên trong
     */
    public static void deleteDirectory(String directoryPath) throws IOException {
        if (directoryPath == null) {
            return;
        }
        Path path = Paths.get(directoryPath);
        if (Files.exists(path)) {
            Files.walk(path)
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }

    /**
     * Di chuyển file
     */
    public static void moveFile(String sourcePath, String targetPath) throws IOException {
        if (sourcePath == null || targetPath == null) {
            return;
        }
        Path source = Paths.get(sourcePath);
        Path target = Paths.get(targetPath);
        Files.move(source, target);
    }

    /**
     * Sao chép file
     */
    public static void copyFile(String sourcePath, String targetPath) throws IOException {
        if (sourcePath == null || targetPath == null) {
            return;
        }
        Path source = Paths.get(sourcePath);
        Path target = Paths.get(targetPath);
        Files.copy(source, target);
    }

    /**
     * Đổi tên file
     */
    public static void renameFile(String oldPath, String newPath) throws IOException {
        if (oldPath == null || newPath == null) {
            return;
        }
        Path source = Paths.get(oldPath);
        Path target = Paths.get(newPath);
        Files.move(source, target);
    }

    /**
     * Lấy kích thước file
     */
    public static long getFileSize(String filePath) throws IOException {
        if (filePath == null) {
            return 0;
        }
        Path path = Paths.get(filePath);
        return Files.size(path);
    }

    /**
     * Lấy kích thước thư mục
     */
    public static long getDirectorySize(String directoryPath) throws IOException {
        if (directoryPath == null) {
            return 0;
        }
        Path path = Paths.get(directoryPath);
        return Files.walk(path)
            .filter(Files::isRegularFile)
            .mapToLong(p -> {
                try {
                    return Files.size(p);
                } catch (IOException e) {
                    return 0;
                }
            })
            .sum();
    }

    /**
     * Kiểm tra file có tồn tại không
     */
    public static boolean exists(String filePath) {
        if (filePath == null) {
            return false;
        }
        return Files.exists(Paths.get(filePath));
    }

    /**
     * Kiểm tra thư mục có tồn tại không
     */
    public static boolean isDirectory(String path) {
        if (path == null) {
            return false;
        }
        return Files.isDirectory(Paths.get(path));
    }

    /**
     * Kiểm tra file có phải là file thông thường không
     */
    public static boolean isRegularFile(String path) {
        if (path == null) {
            return false;
        }
        return Files.isRegularFile(Paths.get(path));
    }

    /**
     * Lấy đường dẫn tuyệt đối của file
     */
    public static String getAbsolutePath(String path) {
        if (path == null) {
            return null;
        }
        return Paths.get(path).toAbsolutePath().toString();
    }

    /**
     * Lấy đường dẫn tương đối của file
     */
    public static String getRelativePath(String basePath, String path) {
        if (basePath == null || path == null) {
            return null;
        }
        Path base = Paths.get(basePath);
        Path target = Paths.get(path);
        return base.relativize(target).toString();
    }

    /**
     * Lấy đường dẫn cha của file
     */
    public static String getParentPath(String path) {
        if (path == null) {
            return null;
        }
        return Paths.get(path).getParent().toString();
    }

    /**
     * Lấy tên file từ đường dẫn
     */
    public static String getFileName(String path) {
        if (path == null) {
            return null;
        }
        return Paths.get(path).getFileName().toString();
    }

    /**
     * Lấy danh sách file trong thư mục
     */
    public static List<String> listFiles(String directoryPath) throws IOException {
        if (directoryPath == null) {
            return null;
        }
        Path path = Paths.get(directoryPath);
        return Files.list(path)
            .map(Path::toString)
            .toList();
    }

    /**
     * Lấy danh sách thư mục trong thư mục
     */
    public static List<String> listDirectories(String directoryPath) throws IOException {
        if (directoryPath == null) {
            return null;
        }
        Path path = Paths.get(directoryPath);
        return Files.list(path)
            .filter(Files::isDirectory)
            .map(Path::toString)
            .toList();
    }

    /**
     * Lấy danh sách file trong thư mục và thư mục con
     */
    public static List<String> listFilesRecursively(String directoryPath) throws IOException {
        if (directoryPath == null) {
            return null;
        }
        Path path = Paths.get(directoryPath);
        return Files.walk(path)
            .filter(Files::isRegularFile)
            .map(Path::toString)
            .toList();
    }

    /**
     * Lấy danh sách thư mục trong thư mục và thư mục con
     */
    public static List<String> listDirectoriesRecursively(String directoryPath) throws IOException {
        if (directoryPath == null) {
            return null;
        }
        Path path = Paths.get(directoryPath);
        return Files.walk(path)
            .filter(Files::isDirectory)
            .map(Path::toString)
            .toList();
    }
} 