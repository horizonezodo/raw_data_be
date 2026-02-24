package com.ngv.zns_service.util.date;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeConverter {
    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Ho_Chi_Minh");

    private static final DateTimeFormatter FORMATTER_YMDHMS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
            .withZone(ZONE_ID);

    private static final DateTimeFormatter FORMATTER_DDMMYYYY_HHMM = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FORMATTER_DDMMYYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String dateTimeNow() {
        ZonedDateTime now = ZonedDateTime.now(ZONE_ID);
        return now.format(FORMATTER_DDMMYYYY_HHMM);
    }

    public String dateNow() {
        ZonedDateTime now = ZonedDateTime.now(ZONE_ID);
        return now.format(FORMATTER_DDMMYYYY);
    }

    // ✅ Hàm mới: chuyển Instant -> "yyyyMMddHHmmss"
    public String formatToYmdHms(Instant instant) {
        return FORMATTER_YMDHMS.format(instant);
    }
}
