package ngvgroup.com.crm.features.customer.common;

import java.util.Date;

import org.springframework.stereotype.Component;

import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.crm.core.constant.CrmVariableConstants;
import ngvgroup.com.crm.core.utils.DateUtils;
import ngvgroup.com.crm.core.utils.SequenceUtil;
import ngvgroup.com.crm.features.customer.model.txn.CrmTxnCust;

@Component
@RequiredArgsConstructor
public class GenerateNextSequence {
    private final SequenceUtil sequenceUtil;

    /**
     * Sinh mã hồ sơ đăng ký (Process Instance)
     * Quy tắc: Prefix.YYMMDD.xxxxx (Có dấu chấm)
     */
    public String generateCustomerRegistration() {
        // Giả sử DateUtils trả về chuỗi "250312" (YYMMDD)
        String currentDate = DateUtils.formatDateToSequence(new Date());

        return sequenceUtil.getNextSequence(
                CrmVariableConstants.PROCESS_KEY_CUSTOMER_REGISTER, // Prefix (VD: CRM.200.01)
                CrmTxnCust.class.getAnnotation(Table.class).name(),
                "PROCESS_INSTANCE_CODE",
                currentDate,
                1,
                5, // Độ dài đuôi 5 số
                "." // Dùng dấu chấm làm vách ngăn
        );
    }

    public String generateCustomerAdjust() {
        // Giả sử DateUtils trả về chuỗi "250312" (YYMMDD)
        String currentDate = DateUtils.formatDateToSequence(new Date());

        return sequenceUtil.getNextSequence(
                CrmVariableConstants.PROCESS_KEY_CUSTOMER_ADJUST, // Prefix (VD: CRM.200.01)
                CrmTxnCust.class.getAnnotation(Table.class).name(),
                "PROCESS_INSTANCE_CODE",
                currentDate,
                1,
                5, // Độ dài đuôi 5 số
                "." // Dùng dấu chấm làm vách ngăn
        );
    }

    /**
     * Sinh Mã Khách Hàng
     * Quy tắc: [MaChiNhanh][XXXXXX] (Viết liền)
     * 
     * @param branchCode Mã chi nhánh (VD: HN01)
     */
    public String generateMaKhachHang(String branchCode) {
        return sequenceUtil.getNextSequence(
                branchCode, // Prefix là Mã chi nhánh
                CrmTxnCust.class.getAnnotation(Table.class).name(),
                "CUSTOMER_CODE",
                null, // Không dùng ngày tháng
                1,
                6, // Độ dài đuôi 6 số (XXXXXX)
                "" // Rỗng -> Viết liền, không có dấu chấm
        );
    }
}
