package ngvgroup.com.hrm.feature.employee.utils;

import com.ngvgroup.bpm.core.common.util.DateUtils;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.hrm.core.constant.HrmVariableConstants;
import ngvgroup.com.hrm.core.util.SequenceUtil;
import ngvgroup.com.hrm.feature.employee.model.txn.HrmTxnEmployee;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class GenerateNextSequence {
    private final SequenceUtil sequenceUtil;
    private static final String DATE_FORMAT = "YYMMDD";

    /**
     * Sinh mã hồ sơ đăng ký (Process Instance)
     * Quy tắc: Prefix.YYMMDD.xxxxx (Có dấu chấm)
     */
    public String generateRegisterProcessInstanceCode() {
        // Giả sử DateUtils trả về chuỗi "250312" (YYMMDD)
        String currentDate = DateUtils.format(new Date(), DATE_FORMAT);

        return sequenceUtil.getNextSequence(
                HrmVariableConstants.PROCESS_KEY_HRM_REGISTER, // Prefix (VD: CRM.200.01)
                HrmTxnEmployee.class.getAnnotation(Table.class).name(),
                "PROCESS_INSTANCE_CODE",
                currentDate,
                1,
                5, // Độ dài đuôi 5 số
                "." // Dùng dấu chấm làm vách ngăn
        );
    }

    public String generateAdjustProcessInstanceCode() {
        // Giả sử DateUtils trả về chuỗi "250312" (YYMMDD)
        String currentDate = DateUtils.format(new Date(), DATE_FORMAT);

        return sequenceUtil.getNextSequence(
                HrmVariableConstants.PROCESS_KEY_HRM_ADJUST, // Prefix (VD: CRM.200.01)
                HrmTxnEmployee.class.getAnnotation(Table.class).name(),
                "PROCESS_INSTANCE_CODE",
                currentDate,
                1,
                5, // Độ dài đuôi 5 số
                "." // Dùng dấu chấm làm vách ngăn
        );
    }

    public String generateEmployeeCode() {
        // Giả sử DateUtils trả về chuỗi "250312" (YYMMDD)
        String currentDate = DateUtils.format(new Date(), DATE_FORMAT);

        return sequenceUtil.getNextSequence(
                HrmVariableConstants.EMPLOYEE_PREFIX, // Prefix (VD: CRM.200.01)
                HrmTxnEmployee.class.getAnnotation(Table.class).name(),
                "EMPLOYEE_CODE",
                currentDate,
                1,
                5, // Độ dài đuôi 5 số
                "." // Dùng dấu chấm làm vách ngăn
        );
    }
}
