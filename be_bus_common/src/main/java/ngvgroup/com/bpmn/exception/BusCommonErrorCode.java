package ngvgroup.com.bpmn.exception;

import com.ngvgroup.bpm.core.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class BusCommonErrorCode {
    public static final ErrorCode REPORT_GROUP_CODE_ALREADY_EXISTS =
            new ErrorCode(409, "Mã nhóm báo cáo đã tồn tại trong báo cáo", HttpStatus.CONFLICT) {};

    public static final ErrorCode SOURCE_PARAM_CODE_ALREADY_EXISTS =
            new ErrorCode(409, "Tham số nguồn đã tồn tại trong điều kiện ràng buộc tham số", HttpStatus.CONFLICT) {};

    public static final ErrorCode TARGET_PARAM_CODE_ALREADY_EXISTS =
            new ErrorCode(409, "Tham số đích đã tồn tại trong điều kiện ràng buộc tham số", HttpStatus.CONFLICT) {};

}
