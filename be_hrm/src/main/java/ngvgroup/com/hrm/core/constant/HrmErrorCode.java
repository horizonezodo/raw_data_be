package ngvgroup.com.hrm.core.constant;

import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class HrmErrorCode {
    private HrmErrorCode(){}

    public static final ErrorCode EXPORT_EXCEL_ERROR =
            new ErrorCode(500, "Lỗi tải file excel", HttpStatus.INTERNAL_SERVER_ERROR) {};

    public static final ErrorCode POSITION_CODE_EXIST =
            new ErrorCode(3001, "Mã chức vụ %s đã được sử dụng", HttpStatus.UNPROCESSABLE_ENTITY) {};

    public static final ErrorCode TITLE_CODE_EXIST =
            new ErrorCode(4001, "Mã chức danh %s đã được sử dụng", HttpStatus.CONFLICT) {};
    public static final ErrorCode TITLE_CODE_IS_USED =
            new ErrorCode(4002, "Mã chức danh %s đã gắn với chức vụ trên hệ thống. Không được xóa", HttpStatus.CONFLICT) {};

    public static final ErrorCode DUPLICATE_UNIT_CODE = new ErrorCode(5000, "Mã phòng ban %s đã tồn tại trong hệ thống",
            HttpStatus.CONFLICT) {
    };
    public static final ErrorCode NOT_FOUND_UNIT_CODE = new ErrorCode(5001, "Mã phòng ban %s không tồn tại trong hệ thống",
            HttpStatus.NOT_FOUND) {
    };

    public static final ErrorCode WRITE_EXCEL_ERROR = new ErrorCode(5002, "Lỗi ghi dữ liệu ra file excel %s ",
            HttpStatus.INTERNAL_SERVER_ERROR) {
    };

    public static final ErrorCode INVALID_DATA_REQUEST = new ErrorCode(5003, "Dữ liệu đầu vào không hợp lệ!",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode DATA_NOT_FOUND = new ErrorCode(5004, "Dữ liệu %s không tồn tại!",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode EMPLOYEE_TXN_NOT_FOUND = new ErrorCode(4005,
            "Không tìm thấy giao dịch nhân viên với mã: %s",
            HttpStatus.NOT_FOUND) {
    };

    public static final ErrorCode PHONE_EXIST = new ErrorCode(4006, "Số điện thoại %s đã tồn tại trên hệ thống",
            HttpStatus.CONFLICT) {
    };

    public static final ErrorCode IDENTIFICATION_ID_EXIST = new ErrorCode(4007,
            "Số định danh %s đã tồn tại trên hệ thống",
            HttpStatus.CONFLICT) {
    };

    public static final ErrorCode INVALID_DATE_RANGE = new ErrorCode(4008,
            "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc: %s",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode MISSING_REQUIRED_FIELD = new ErrorCode(5005,
            "Thông tin bắt buộc không được để trống: %s",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode INVALID_EXPIRY_DATE = new ErrorCode(4009,
            "Ngày hết hiệu lực không hợp lệ.",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode INVALID_CONFIRM_DATE = new ErrorCode(4010,
            "Ngày chính thức không hợp lệ.",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode INVALID_POSITION_VALIDITY = new ErrorCode(4011,
            "Thời gian hiệu lực của chức vụ không hợp lệ.",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode DUPLICATE_POSITION = new ErrorCode(4012,
            "Thông tin chức vụ đã tồn tại.",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode OVERLAPPING_POSITION_VALIDITY = new ErrorCode(4013,
            "Vui lòng chỉ khai báo một chức vụ tại một thời điểm.",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode MISSING_ACTIVE_POSITION = new ErrorCode(4014,
            "Vui lòng khai báo thông tin chức vụ còn hiệu lực.",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode INVALID_EDUCATION_YEAR = new ErrorCode(4015,
            "Năm học không hợp lệ.",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode MISSING_POSITION_INFO = new ErrorCode(4016,
            "Vui lòng khai báo ít nhất một thông tin chức vụ.",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode MISSING_EDUCATION_INFO = new ErrorCode(4017,
            "Vui lòng khai báo ít nhất một thông tin học vấn.",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode BUSINESS_DATA_MISSING = new ErrorCode(4018,
            "Dữ liệu nghiệp vụ không tồn tại.",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode INVALID_APPROVAL_STATUS = new ErrorCode(4019,
            "Trạng thái phê duyệt không hợp lệ: %s",
            HttpStatus.BAD_REQUEST) {
    };
}
