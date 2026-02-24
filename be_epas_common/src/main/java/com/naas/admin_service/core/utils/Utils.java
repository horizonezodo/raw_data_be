package com.naas.admin_service.core.utils;

import com.naas.admin_service.core.contants.Constant;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class Utils {

    private Utils() {}

    public static String convertDate(Date dateTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(dateTime);
    }

    public static String convertUnixTimestamp(String timestampInSeconds) {
        long ts = Long.parseLong(timestampInSeconds);
        Instant instant = Instant.ofEpochSecond(ts);
        ZonedDateTime vietnamTime = instant.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
        return formatter.format(vietnamTime);
    }

    public static String buildTemplatePath(String templateCode) {
        return Constant.TEMPLATE_PATH + templateCode + "/";
    }
}
