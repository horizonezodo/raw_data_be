package com.ngvgroup.bpm.core.common.util;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Tiện ích xử lý chuỗi
 */
public class StringUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10,11}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Kiểm tra chuỗi rỗng
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Kiểm tra chuỗi không rỗng
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Kiểm tra chuỗi blank
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Kiểm tra chuỗi không blank
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * Cắt chuỗi theo độ dài
     */
    public static String truncate(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        return str.length() <= maxLength ? str : str.substring(0, maxLength);
    }

    /**
     * Cắt chuỗi theo độ dài và thêm dấu ...
     */
    public static String truncateWithEllipsis(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    /**
     * Chuyển đổi chuỗi thành số
     */
    public static Integer toInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Chuyển đổi chuỗi thành số thực
     */
    public static Double toDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Chuyển đổi chuỗi thành boolean
     */
    public static Boolean toBoolean(String str) {
        if (str == null) {
            return null;
        }
        return Boolean.parseBoolean(str);
    }

    /**
     * Tạo UUID
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Tạo UUID không có dấu gạch ngang
     */
    public static String generateUUIDWithoutDash() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Kiểm tra email hợp lệ
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Kiểm tra số điện thoại hợp lệ
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Kiểm tra mật khẩu hợp lệ
     */
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Chuyển đổi camelCase thành snake_case
     */
    public static String camelToSnake(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    /**
     * Chuyển đổi snake_case thành camelCase
     */
    public static String snakeToCamel(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        return result.toString();
    }

    /**
     * Chuyển đổi chuỗi thành PascalCase
     */
    public static String toPascalCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean nextUpper = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_' || c == ' ') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        return result.toString();
    }

    /**
     * Chuyển đổi chuỗi thành Title Case
     */
    public static String toTitleCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean nextUpper = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_' || c == ' ') {
                result.append(' ');
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        return result.toString();
    }
} 