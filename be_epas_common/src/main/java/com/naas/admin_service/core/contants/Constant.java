package com.naas.admin_service.core.contants;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;

public class Constant {
    public static final String ORG = "%";

    private Constant() {
    }
    private static final List<String> START_PRIORITY = Arrays.asList("CM084.001", "CM084.002", "CM084.003");
    private static final List<String> END_PRIORITY = Arrays.asList("CM084.003", "CM084.002", "CM084.001");

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    public static final String APPROVAL = "APPROVAL";
    public static final String CREATE = "CREATE";
    public static final String SYSTEM = "SYSTEM";
    public static final String DATE_PATTERN = "yyMMdd";
    // symbol
    public static final String COMMA = ",";
    public static final String REALMS = "/realms/";
    public static final String OPENID_CONNECT_TOKEN = "/protocol/openid-connect/token";
    public static final String GRANT_TYPE = "grant_type";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String OPENID_EMAIL_PROFILE = "openid email profile organization";
    public static final String RESPONSE_TYPE = "code";
    public static final String SCOPE = "scope";
    public static final String EXIT_PARAMS_LOG = "Exit: {%s}";
    public static final String PASSWORD_BLACK_LIST = "passwordBlacklist(";
    public static final String TEMPLATE_PATH = "template/";
    public static final String WEEKEND = "CUOI_TUAN";
    public static final String ACTIVE = "ACTIVE";
    public static final String PENDING = "PENDING";
    public static final String LOGGED_OUT = "LOGGED_OUT";
    public static final String KICKED = "KICKED";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS =
            "Access-Control-Expose-Headers";
    public static final String ATTACHMENT = "attachment; filename=\"";
    public static final String FORMAT_WORD = "word";
    public static final String FORMAT_DOCX = "docx";
    public static final String FORMAT_XLSX = "xlsx";
    public static final String FORMAT_EXCEL = "excel";
    public static final String EXTENSION_DOCX = "docx";
    public static final String EXTENSION_XLSX = "xlsx";
    public static final String EXTENSION_PDF = "pdf";

    public static List<String> getStartPriority() {
        return START_PRIORITY;
    }

    public static List<String> getEndPriority() {
        return END_PRIORITY;
    }

    public static String resolveReportExtension(String format) {
        if (format == null || format.isBlank()) {
            return EXTENSION_PDF;
        }
        if (FORMAT_WORD.equalsIgnoreCase(format)
                || FORMAT_DOCX.equalsIgnoreCase(format)) {
            return EXTENSION_DOCX;
        }
        if (FORMAT_XLSX.equalsIgnoreCase(format)
                || FORMAT_EXCEL.equalsIgnoreCase(format)) {
            return EXTENSION_XLSX;
        }
        return EXTENSION_PDF;
    }

    public static MediaType resolveReportMediaType(String format) {
        String extension = resolveReportExtension(format);
        return switch (extension) {
            case EXTENSION_DOCX ->
                    MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            case EXTENSION_XLSX ->
                    MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            default -> MediaType.APPLICATION_PDF;
        };
    }
}
