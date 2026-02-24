package ngvgroup.com.rpt.features.transactionreport.common;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import org.springframework.stereotype.Service;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;

@Service
public class CalculateDeadlineDate {
    public LocalDate calculate(LocalDate reportDate, String commonValue) {
        if (commonValue.startsWith("YM")) {
            // YM5 = ngày 5 của tháng kế tiếp
            int day = Integer.parseInt(commonValue.substring(2));
            return reportDate.plusMonths(1).withDayOfMonth(day);
        } else if (commonValue.startsWith("YD")) {
            // YD10 = ngày 10 cùng tháng
            int day = Integer.parseInt(commonValue.substring(2));
            return reportDate.withDayOfMonth(day);
        } else if (commonValue.startsWith("YQ")) {
            // YQ1 = ngày đầu quý kế tiếp
            int nextQuarter = ((reportDate.getMonthValue() - 1) / 3 + 1) * 3 + 1;
            int year = reportDate.getYear();
            if (nextQuarter > 12) {
                nextQuarter -= 12;
                year += 1;
            }
            return LocalDate.of(year, nextQuarter, 1);
        } else if (commonValue.startsWith("YW")) {
            // YW3 = thứ 3 của tuần kế tiếp
            int dayOfWeek = Integer.parseInt(commonValue.substring(2)); // 1=Mon ... 7=Sun
            return reportDate.plusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.of(dayOfWeek)));
        } else {
            throw new BusinessException(ErrorCode.BAD_REQUEST, commonValue);
        }

    }
}
