package ngvgroup.com.loan.core.utils;

import ngvgroup.com.loan.core.constant.DateConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private DateUtils() {}

    public static String formatDateToSequence(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DateConstants.YYYYMMDD);
        return formatter.format(date);
    }
}
