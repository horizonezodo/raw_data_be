package ngvgroup.com.crm.core.constant;

import org.springframework.http.HttpStatus;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;

/**
 * Consolidated error constants including:
 * - Business error codes
 * - Category error codes
 */
public class CrmErrorCode {
        private CrmErrorCode() {
                throw new BusinessException(new ErrorCode(5015, "Utility class", HttpStatus.INTERNAL_SERVER_ERROR) {
                });
        }

        // Validation errors
        public static final ErrorCode VALIDATION_ERROR = new ErrorCode(4000, "Dữ liệu đầu vào không chính xác",
                        HttpStatus.UNPROCESSABLE_ENTITY) {
        };

        // Business errors
        public static final ErrorCode NOT_NULL = new ErrorCode(5001, "Giá trị không được để trống",
                        HttpStatus.INTERNAL_SERVER_ERROR) {
        };
        public static final ErrorCode MOBILE_NUMBER_EXIST = new ErrorCode(5002, "Số điện thoại đã tồn tại",
                        HttpStatus.INTERNAL_SERVER_ERROR) {
        };
        public static final ErrorCode CCCD_EXIST = new ErrorCode(5003, "Số CCCD đã tồn tại",
                        HttpStatus.INTERNAL_SERVER_ERROR) {
        };

        public static final ErrorCode WRITE_EXCEL_ERROR = new ErrorCode(5002, "Lỗi ghi dữ liệu ra file excel %s ",
                        HttpStatus.INTERNAL_SERVER_ERROR) {
        };

        public static final ErrorCode INVALID_JSON_FORMAT = new ErrorCode(4001, "Định dạng JSON không hợp lệ: %s",
                        HttpStatus.BAD_REQUEST) {
        };

        public static final ErrorCode INVALID_APPROVAL_STATUS = new ErrorCode(4002,
                        "Trạng thái phê duyệt không hợp lệ: %s",
                        HttpStatus.BAD_REQUEST) {
        };

        public static final ErrorCode BUSINESS_DATA_MISSING = new ErrorCode(4004, "Không tìm thấy dữ liệu nghiệp vụ",
                        HttpStatus.BAD_REQUEST) {
        };
        public static final ErrorCode PROCESS_NOT_FOUND = new ErrorCode(4005, "Không tìm thấy hồ sơ process",
                        HttpStatus.NOT_FOUND) {
        };
        public static final ErrorCode INVALID_REQUEST_DATA = new ErrorCode(4006, "Dữ liệu yêu cầu không hợp lệ",
                        HttpStatus.BAD_REQUEST) {
        };
        public static final ErrorCode INVALID_TASK_INFO = new ErrorCode(4007, "Thông tin task không hợp lệ",
                        HttpStatus.BAD_REQUEST) {
        };
        public static final ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode(5000, "Lỗi hệ thống",
                        HttpStatus.INTERNAL_SERVER_ERROR) {
        };
        public static final ErrorCode MAIL_DATA_ERROR = new ErrorCode(5010, "Lỗi tạo dữ liệu mail",
                        HttpStatus.INTERNAL_SERVER_ERROR) {
        };
        public static final ErrorCode MISSING_CUSTOMER_DATA = new ErrorCode(4010,
                        "Dữ liệu khách hàng không được để trống",
                        HttpStatus.BAD_REQUEST) {
        };
        public static final ErrorCode MISSING_INSURANCE_INFO = new ErrorCode(4011,
                        "Thông tin bảo hiểm tiền gửi là bắt buộc", HttpStatus.BAD_REQUEST) {
        };
        public static final ErrorCode MISSING_PERSONAL_INFO = new ErrorCode(4012, "Thiếu thông tin cá nhân",
                        HttpStatus.BAD_REQUEST) {
        };
        public static final ErrorCode MISSING_CORPORATE_INFO = new ErrorCode(4013, "Thiếu thông tin doanh nghiệp",
                        HttpStatus.BAD_REQUEST) {
        };
        public static final ErrorCode MISSING_ADDRESS = new ErrorCode(4014, "Phải nhập ít nhất một địa chỉ",
                        HttpStatus.BAD_REQUEST) {
        };
        public static final ErrorCode INVALID_PRIMARY_ADDRESS = new ErrorCode(4015,
                        "Phải có duy nhất một địa chỉ chính",
                        HttpStatus.BAD_REQUEST) {
        };
        public static final ErrorCode MISSING_WORK_TIME_UNIT = new ErrorCode(4016,
                        "Vui lòng chọn đơn vị tính cho thâm niên công tác", HttpStatus.BAD_REQUEST) {
        };

        public static final ErrorCode CUSTOMER_NOT_FOUND = new ErrorCode(4017, "Không tìm thấy khách hàng",
                        HttpStatus.NOT_FOUND) {
        };
}
