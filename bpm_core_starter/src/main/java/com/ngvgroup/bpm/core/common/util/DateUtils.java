package com.ngvgroup.bpm.core.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Tiện ích xử lý ngày tháng
 */
public class DateUtils {

    private static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";
    private static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";
    private static final String DEFAULT_DATETIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
    private static final String ISO_DATE_PATTERN = "yyyy-MM-dd";
    private static final String ISO_TIME_PATTERN = "HH:mm:ss";
    private static final String ISO_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    private DateUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Chuyển đổi Date thành LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Chuyển đổi LocalDateTime thành Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Chuyển đổi Date thành LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Chuyển đổi LocalDate thành Date
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Chuyển đổi Date thành LocalTime
     */
    public static LocalTime toLocalTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * Chuyển đổi LocalTime thành Date
     */
    public static Date toDate(LocalTime localTime) {
        if (localTime == null) {
            return null;
        }
        return Date.from(localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Format LocalDateTime theo pattern
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Format LocalDate theo pattern
     */
    public static String format(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Format LocalTime theo pattern
     */
    public static String format(LocalTime time, String pattern) {
        if (time == null) {
            return null;
        }
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Format Date theo pattern
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return format(toLocalDateTime(date), pattern);
    }

    /**
     * Parse chuỗi thành LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        if (dateTimeStr == null) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Parse chuỗi thành LocalDate
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        if (dateStr == null) {
            return null;
        }
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Parse chuỗi thành LocalTime
     */
    public static LocalTime parseTime(String timeStr, String pattern) {
        if (timeStr == null) {
            return null;
        }
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Parse chuỗi thành Date
     */
    public static Date parse(String dateStr, String pattern) {
        if (dateStr == null) {
            return null;
        }
        return toDate(parseDateTime(dateStr, pattern));
    }

    /**
     * Format LocalDateTime theo pattern mặc định
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return format(dateTime, DEFAULT_DATETIME_PATTERN);
    }

    /**
     * Format LocalDate theo pattern mặc định
     */
    public static String formatDate(LocalDate date) {
        return format(date, DEFAULT_DATE_PATTERN);
    }

    /**
     * Format LocalTime theo pattern mặc định
     */
    public static String formatTime(LocalTime time) {
        return format(time, DEFAULT_TIME_PATTERN);
    }

    /**
     * Format Date theo pattern mặc định
     */
    public static String formatDateTime(Date date) {
        return format(date, DEFAULT_DATETIME_PATTERN);
    }

    /**
     * Format Date theo pattern mặc định
     */
    public static String formatDate(Date date) {
        return format(date, DEFAULT_DATE_PATTERN);
    }

    /**
     * Format Date theo pattern mặc định
     */
    public static String formatTime(Date date) {
        return format(date, DEFAULT_TIME_PATTERN);
    }

    /**
     * Parse chuỗi thành LocalDateTime theo pattern mặc định
     */
    public static LocalDateTime parseDefaultDateTime(String dateTimeStr) {
        return parseDateTime(dateTimeStr, DEFAULT_DATETIME_PATTERN);
    }

    /**
     * Parse chuỗi thành LocalDate theo pattern mặc định
     */
    public static LocalDate parseDefaultDate(String dateStr) {
        return parseDate(dateStr, DEFAULT_DATE_PATTERN);
    }

    /**
     * Parse chuỗi thành LocalTime theo pattern mặc định
     */
    public static LocalTime parseDefaultTime(String timeStr) {
        return parseTime(timeStr, DEFAULT_TIME_PATTERN);
    }

    /**
     * Parse chuỗi thành Date theo pattern mặc định
     */
    public static Date parseDefaultDateTimeToDate(String dateTimeStr) {
        return parse(dateTimeStr, DEFAULT_DATETIME_PATTERN);
    }

    /**
     * Parse chuỗi thành Date theo pattern mặc định
     */
    public static Date parseDefaultDateToDate(String dateStr) {
        return parse(dateStr, DEFAULT_DATE_PATTERN);
    }

    /**
     * Parse chuỗi thành Date theo pattern mặc định
     */
    public static Date parseDefaultTimeToDate(String timeStr) {
        return parse(timeStr, DEFAULT_TIME_PATTERN);
    }

    /**
     * Format LocalDateTime theo ISO pattern
     */
    public static String formatIsoDateTime(LocalDateTime dateTime) {
        return format(dateTime, ISO_DATETIME_PATTERN);
    }

    /**
     * Format LocalDate theo ISO pattern
     */
    public static String formatIsoDate(LocalDate date) {
        return format(date, ISO_DATE_PATTERN);
    }

    /**
     * Format LocalTime theo ISO pattern
     */
    public static String formatIsoTime(LocalTime time) {
        return format(time, ISO_TIME_PATTERN);
    }

    /**
     * Format Date theo ISO pattern
     */
    public static String formatIsoDateTime(Date date) {
        return format(date, ISO_DATETIME_PATTERN);
    }

    /**
     * Format Date theo ISO pattern
     */
    public static String formatIsoDate(Date date) {
        return format(date, ISO_DATE_PATTERN);
    }

    /**
     * Format Date theo ISO pattern
     */
    public static String formatIsoTime(Date date) {
        return format(date, ISO_TIME_PATTERN);
    }

    /**
     * Parse chuỗi thành LocalDateTime theo ISO pattern
     */
    public static LocalDateTime parseIsoDateTime(String dateTimeStr) {
        return parseDateTime(dateTimeStr, ISO_DATETIME_PATTERN);
    }

    /**
     * Parse chuỗi thành LocalDate theo ISO pattern
     */
    public static LocalDate parseIsoDate(String dateStr) {
        return parseDate(dateStr, ISO_DATE_PATTERN);
    }

    /**
     * Parse chuỗi thành LocalTime theo ISO pattern
     */
    public static LocalTime parseIsoTime(String timeStr) {
        return parseTime(timeStr, ISO_TIME_PATTERN);
    }

    /**
     * Parse chuỗi thành Date theo ISO pattern
     */
    public static Date parseIsoDateTimeToDate(String dateTimeStr) {
        return parse(dateTimeStr, ISO_DATETIME_PATTERN);
    }

    /**
     * Parse chuỗi thành Date theo ISO pattern
     */
    public static Date parseIsoDateToDate(String dateStr) {
        return parse(dateStr, ISO_DATE_PATTERN);
    }

    /**
     * Parse chuỗi thành Date theo ISO pattern
     */
    public static Date parseIsoTimeToDate(String timeStr) {
        return parse(timeStr, ISO_TIME_PATTERN);
    }

    /**
     * Tính số ngày giữa hai ngày
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * Tính số giờ giữa hai thời điểm
     */
    public static long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }

    /**
     * Tính số phút giữa hai thời điểm
     */
    public static long minutesBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }

    /**
     * Tính số giây giữa hai thời điểm
     */
    public static long secondsBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.SECONDS.between(startDateTime, endDateTime);
    }

    /**
     * Tính số ngày giữa hai ngày
     */
    public static long daysBetween(Date startDate, Date endDate) {
        return daysBetween(toLocalDate(startDate), toLocalDate(endDate));
    }

    /**
     * Tính số giờ giữa hai thời điểm
     */
    public static long hoursBetween(Date startDate, Date endDate) {
        return hoursBetween(toLocalDateTime(startDate), toLocalDateTime(endDate));
    }

    /**
     * Tính số phút giữa hai thời điểm
     */
    public static long minutesBetween(Date startDate, Date endDate) {
        return minutesBetween(toLocalDateTime(startDate), toLocalDateTime(endDate));
    }

    /**
     * Tính số giây giữa hai thời điểm
     */
    public static long secondsBetween(Date startDate, Date endDate) {
        return secondsBetween(toLocalDateTime(startDate), toLocalDateTime(endDate));
    }

    /**
     * Lấy ngày hiện tại
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    /**
     * Lấy thời gian hiện tại
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * Lấy thời gian hiện tại
     */
    public static Date nowDate() {
        return toDate(now());
    }

    /**
     * Lấy ngày đầu tiên của tháng
     */
    public static LocalDate firstDayOfMonth(LocalDate date) {
        return date.withDayOfMonth(1);
    }

    /**
     * Lấy ngày cuối cùng của tháng
     */
    public static LocalDate lastDayOfMonth(LocalDate date) {
        return date.withDayOfMonth(date.lengthOfMonth());
    }

    /**
     * Lấy ngày đầu tiên của năm
     */
    public static LocalDate firstDayOfYear(LocalDate date) {
        return date.withDayOfYear(1);
    }

    /**
     * Lấy ngày cuối cùng của năm
     */
    public static LocalDate lastDayOfYear(LocalDate date) {
        return date.withDayOfYear(date.lengthOfYear());
    }

    /**
     * Lấy ngày đầu tiên của tháng
     */
    public static Date firstDayOfMonth(Date date) {
        return toDate(firstDayOfMonth(toLocalDate(date)));
    }

    /**
     * Lấy ngày cuối cùng của tháng
     */
    public static Date lastDayOfMonth(Date date) {
        return toDate(lastDayOfMonth(toLocalDate(date)));
    }

    /**
     * Lấy ngày đầu tiên của năm
     */
    public static Date firstDayOfYear(Date date) {
        return toDate(firstDayOfYear(toLocalDate(date)));
    }

    /**
     * Lấy ngày cuối cùng của năm
     */
    public static Date lastDayOfYear(Date date) {
        return toDate(lastDayOfYear(toLocalDate(date)));
    }
} 