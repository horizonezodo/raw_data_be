package ngvgroup.com.bpmn.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class SequenceUtil {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Lấy giá trị tiếp theo từ sequence của Oracle.
     * @param sequenceName Tên sequence trong database (Ví dụ: DKKH001_YYYYMMDD_SEQ)
     * @return Giá trị nextVal của sequence
     */
    public Long getNextSequenceValue(String sequenceName) {
        return ((BigDecimal) entityManager
                .createNativeQuery("SELECT " + sequenceName + ".NEXTVAL FROM DUAL")
                .getSingleResult()).longValue();
    }

    /**
     * Lấy ngày hiện tại theo format YYYYmmDD
     * @return Chuỗi ngày hiện tại (YYYYmmDD)
     */
    public String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * Sinh mã định danh theo format [prefix].[YYYYmmDD].[nextVal]
     * @param prefix Mã quy trình (Ví dụ: DKKH001)
     * @return Mã sinh tự động theo format [prefix].[YYYYmmDD].[nextVal]
     */
    public String generateCode(String prefix) {
        String currentDate = getCurrentDate();
        if ("DKKH001".equals(prefix)) {
            Long nextVal = getNextSequenceValue("SEQ_DKKH");
            return prefix + "." + currentDate + "." + String.format("%05d", nextVal);
        }
        return null;
    }

    public String generateMaKhachHang() {
        Long nextVal = getNextSequenceValue("SEQ_CRM_TXN_CUSTOMER");
        return String.format("%05d", nextVal);
    }
}
