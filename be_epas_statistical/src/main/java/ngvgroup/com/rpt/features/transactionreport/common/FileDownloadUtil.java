package ngvgroup.com.rpt.features.transactionreport.common;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class FileDownloadUtil {
    // MIME Type Constants
    private static final String MIME_OCTET_STREAM = MediaType.APPLICATION_OCTET_STREAM_VALUE;
    private static final String MIME_PDF = MediaType.APPLICATION_PDF_VALUE;
    private static final String MIME_HTML = MediaType.TEXT_HTML_VALUE;
    private static final String MIME_PNG = MediaType.IMAGE_PNG_VALUE;
    private static final String MIME_JPEG = MediaType.IMAGE_JPEG_VALUE;
    private static final String MIME_PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    private static final String MIME_PPT = "application/vnd.ms-powerpoint";
    private static final String MIME_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private FileDownloadUtil() {
        // Prevent instantiation
    }

    /**
     * Detect MIME type based on file extension.
     * Note: Returns text/html for Excel/Word/Docx as they are converted for preview in buildFileResponse.
     */
    public static String getContentType(String fileName) {
        if (fileName == null) {
            return MIME_OCTET_STREAM;
        }

        String lower = fileName.toLowerCase();

        if (lower.endsWith(".pdf")) {
            return MIME_PDF;
        } else if (lower.endsWith(".xlsx") || lower.endsWith(".xls")) {
            return MIME_HTML; // Excel -> preview HTML
        } else if (lower.endsWith(".docx") || lower.endsWith(".doc")) {
            return MIME_HTML; // Word -> preview HTML
        } else if (lower.endsWith(".pptx")) {
            return MIME_PPTX;
        } else if (lower.endsWith(".ppt")) {
            return MIME_PPT;
        } else if (lower.endsWith(".png")) {
            return MIME_PNG;
        } else if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
            return MIME_JPEG;
        } else {
            return MIME_OCTET_STREAM;
        }
    }

    // Build ResponseEntity for download or HTML preview for Excel
    public static ResponseEntity<byte[]> buildFileResponse(String fileName, byte[] data) {
        if (fileName == null) {
            throw new IllegalArgumentException("fileName must not be null");
        }

        try {
            String lower = fileName.toLowerCase();
            byte[] responseData;

            // Handle Excel/Word -> convert to HTML for FE preview
            if (lower.endsWith(".xlsx") || lower.endsWith(".xls")) {
                responseData = ReadExcelUtil.convertExcelToHtml(data);
            } else if (lower.endsWith(".doc") || lower.endsWith(".docx")) {
                responseData = ReadWordUtil.convertWordToHtml(data);
            } else {
                // Keep other files as-is
                responseData = data;
            }

            String contentType = getContentType(fileName);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(responseData);

        } catch (Exception e) {
            throw new BusinessException(StatisticalErrorCode.BAD_READ_FILE, fileName, e);
        }
    }

    public static ResponseEntity<byte[]> buildXlsxResponse(String fileName, byte[] data) {
        if (fileName == null) {
            throw new IllegalArgumentException("fileName must not be null");
        }

        try {
            // Return original file without HTML conversion
            return ResponseEntity.ok()
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, MIME_XLSX)
                    .body(data);

        } catch (Exception e) {
            throw new BusinessException(StatisticalErrorCode.BAD_READ_FILE, fileName, e);
        }
    }
}