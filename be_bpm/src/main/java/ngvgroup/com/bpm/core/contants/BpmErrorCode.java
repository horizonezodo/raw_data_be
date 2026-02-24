package ngvgroup.com.bpm.core.contants;

import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class BpmErrorCode {

    private BpmErrorCode() {
    }

    public static final ErrorCode OUT_OF_ORG = new ErrorCode(467, "Chi nhánh này không thuộc sự quản lý của bạn",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode CHECK_ACCESS_INCORRECT = new ErrorCode(5010,
            "Người dùng không có quyền khởi tạo đơn vị này !", HttpStatus.INTERNAL_SERVER_ERROR) {
    };
    public static final ErrorCode RULE_CODE_ALREADY_EXISTS = new ErrorCode(4097, "Mã rule chia bài đã tồn tại",
            HttpStatus.CONFLICT) {
    };
    public static final ErrorCode ALREADY_EXISTS = new ErrorCode(455, "Dữ liệu đã tồn tại", HttpStatus.CONFLICT) {
    };
    public static final ErrorCode UNABLE_GET_LIST_PROCESS = new ErrorCode(4103,
            "Không thể lấy danh sách SLA lúc này. Vui lòng thử lại sau", HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode WRITE_EXCEL_ERROR = new ErrorCode(5002, "Lỗi ghi dữ liệu ra file excel %s ",
            HttpStatus.INTERNAL_SERVER_ERROR) {
    };

    public static final ErrorCode MISSING_REQUIRED_VARS = new ErrorCode(4001,
            "Thiếu biến bắt buộc: orgCode hoặc processInstanceCode", HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode TASK_CREATION_ERROR = new ErrorCode(5003, "Lỗi trong quá trình tạo công việc: %s",
            HttpStatus.INTERNAL_SERVER_ERROR) {
    };

    public static final ErrorCode PARAM_NOT_FOUND = new ErrorCode(4004, "Không tìm thấy tham số cấu hình: %s",
            HttpStatus.NOT_FOUND) {
    };
    public static final ErrorCode PROCESS_FILE_MISSING = new ErrorCode(4005, "File bắt buộc chưa được tải lên: %s",
            HttpStatus.NOT_FOUND) {
    };
    public static final ErrorCode USER_CANNOT_CLAIM_TASK = new ErrorCode(4006,
            "Người dùng không có quyền thực hiện giao dịch này", HttpStatus.BAD_REQUEST) {
    };
}
