package ngvgroup.com.crm.core.utils;

import com.ngvgroup.bpm.core.common.exception.BusinessException;

import ngvgroup.com.crm.core.constant.CrmErrorCode;
import ngvgroup.com.crm.core.constant.DateConstants;

import java.text.ParseException;
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

    public static String formatDateToYMD(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DateConstants.HYPHEN_YYYYMMDD);
        return formatter.format(date);
    }

    public static Date parseDateFromYMD(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(DateConstants.HYPHEN_YYYYMMDD);
            return formatter.parse(dateString);
        } catch (ParseException e) {
            throw new BusinessException(CrmErrorCode.VALIDATION_ERROR, "Định dạng ngày không hợp lệ, yêu cầu định dạng yyyy-MM-dd");
        }
    }




    public static String formatDateYMDToDMY(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(DateConstants.HYPHEN_YYYYMMDD);
            SimpleDateFormat outputFormat = new SimpleDateFormat(DateConstants.HYPHEN_DDMMYYYY);

            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            throw new BusinessException(CrmErrorCode.VALIDATION_ERROR, "Định dạng ngày không hợp lệ, yêu cầu định dạng yyyy-MM-dd");
        }
    }

    public static Date parseYMDToDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new BusinessException(CrmErrorCode.VALIDATION_ERROR, "Định dạng ngày không hợp lệ, yêu cầu định dạng yyyy-MM-dd");
        }
    }

}
