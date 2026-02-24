package com.ngvgroup.bpm.core.common.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Tiện ích validate
 */
public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10,11}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    private static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$");
    private static final Pattern IPV4_PATTERN = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    private static final Pattern IPV6_PATTERN = Pattern.compile("^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final Pattern TIME_PATTERN = Pattern.compile("^\\d{2}:\\d{2}:\\d{2}$");
    private static final Pattern DATETIME_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$");

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    private ValidationUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Validate object với Bean Validation
     */
    public static <T> Set<ConstraintViolation<T>> validate(T object) {
        return validator.validate(object);
    }

    /**
     * Validate object với Bean Validation và groups
     */
    public static <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        return validator.validate(object, groups);
    }

    /**
     * Validate property với Bean Validation
     */
    public static <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName) {
        return validator.validateProperty(object, propertyName);
    }

    /**
     * Validate property với Bean Validation và groups
     */
    public static <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
        return validator.validateProperty(object, propertyName, groups);
    }

    /**
     * Validate value với Bean Validation
     */
    public static <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value) {
        return validator.validateValue(beanType, propertyName, value);
    }

    /**
     * Validate value với Bean Validation và groups
     */
    public static <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
        return validator.validateValue(beanType, propertyName, value, groups);
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
     * Kiểm tra URL hợp lệ
     */
    public static boolean isValidUrl(String url) {
        if (url == null) {
            return false;
        }
        return URL_PATTERN.matcher(url).matches();
    }

    /**
     * Kiểm tra IPv4 hợp lệ
     */
    public static boolean isValidIpv4(String ip) {
        if (ip == null) {
            return false;
        }
        return IPV4_PATTERN.matcher(ip).matches();
    }

    /**
     * Kiểm tra IPv6 hợp lệ
     */
    public static boolean isValidIpv6(String ip) {
        if (ip == null) {
            return false;
        }
        return IPV6_PATTERN.matcher(ip).matches();
    }

    /**
     * Kiểm tra màu hex hợp lệ
     */
    public static boolean isValidHexColor(String color) {
        if (color == null) {
            return false;
        }
        return HEX_COLOR_PATTERN.matcher(color).matches();
    }

    /**
     * Kiểm tra ngày hợp lệ
     */
    public static boolean isValidDate(String date) {
        if (date == null) {
            return false;
        }
        return DATE_PATTERN.matcher(date).matches();
    }

    /**
     * Kiểm tra thời gian hợp lệ
     */
    public static boolean isValidTime(String time) {
        if (time == null) {
            return false;
        }
        return TIME_PATTERN.matcher(time).matches();
    }

    /**
     * Kiểm tra ngày giờ hợp lệ
     */
    public static boolean isValidDateTime(String dateTime) {
        if (dateTime == null) {
            return false;
        }
        return DATETIME_PATTERN.matcher(dateTime).matches();
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
     * Kiểm tra số nguyên
     */
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Kiểm tra số thực
     */
    public static boolean isDouble(String str) {
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Kiểm tra boolean
     */
    public static boolean isBoolean(String str) {
        if (str == null) {
            return false;
        }
        return str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false");
    }

    /**
     * Kiểm tra số trong khoảng
     */
    public static boolean isInRange(int number, int min, int max) {
        return number >= min && number <= max;
    }

    /**
     * Kiểm tra số trong khoảng
     */
    public static boolean isInRange(double number, double min, double max) {
        return number >= min && number <= max;
    }

    /**
     * Kiểm tra độ dài chuỗi trong khoảng
     */
    public static boolean isLengthInRange(String str, int min, int max) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        return length >= min && length <= max;
    }

    /**
     * Kiểm tra chuỗi chứa ký tự đặc biệt
     */
    public static boolean containsSpecialChar(String str) {
        if (str == null) {
            return false;
        }
        return str.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }

    /**
     * Kiểm tra chuỗi chứa số
     */
    public static boolean containsDigit(String str) {
        if (str == null) {
            return false;
        }
        return str.matches(".*\\d.*");
    }

    /**
     * Kiểm tra chuỗi chứa chữ hoa
     */
    public static boolean containsUpperCase(String str) {
        if (str == null) {
            return false;
        }
        return str.matches(".*[A-Z].*");
    }

    /**
     * Kiểm tra chuỗi chứa chữ thường
     */
    public static boolean containsLowerCase(String str) {
        if (str == null) {
            return false;
        }
        return str.matches(".*[a-z].*");
    }

    /**
     * Kiểm tra chuỗi chứa chữ cái
     */
    public static boolean containsLetter(String str) {
        if (str == null) {
            return false;
        }
        return str.matches(".*[a-zA-Z].*");
    }

    /**
     * Kiểm tra chuỗi chứa khoảng trắng
     */
    public static boolean containsWhitespace(String str) {
        if (str == null) {
            return false;
        }
        return str.matches(".*\\s.*");
    }
} 