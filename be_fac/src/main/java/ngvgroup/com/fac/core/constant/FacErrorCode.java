package ngvgroup.com.fac.core.constant;

import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class FacErrorCode {
    private FacErrorCode(){}
    public static final ErrorCode DUPLICATE_COA_ACC_CODE = new ErrorCode(5000, "Mã tài khoản %s tiêu chuẩn đã tồn tại trong hệ thống",
            HttpStatus.CONFLICT) {
    };
    public static final ErrorCode NOT_FOUND_COA_ACC_CODE = new ErrorCode(5001, "Mã %s tài khoản tiêu chuẩn không tồn tại trong hệ thống",
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

    public static final ErrorCode DOMAIN_CODE_REQUIRED =
            new ErrorCode(4009, "Chủ đề bắt buộc", HttpStatus.BAD_REQUEST){};

    public static final ErrorCode ACC_PURPOSE_NOT_FOUND =
            new ErrorCode(
                    4101,
                    "Mã mục đích tài khoản không tồn tại",
                    HttpStatus.BAD_REQUEST
            ) {
            };

    public static final ErrorCode MAP_VALUE_NOT_FOUND =
            new ErrorCode(
                    4201,
                    "Không tìm thấy cấu hình mapping cho mã %s",
                    HttpStatus.BAD_REQUEST
            ){};

    public static final ErrorCode INVALID_SEGMENT_TYPE =
            new ErrorCode(
                    4301,
                    "Loại phân đoạn cấu trúc tài khoản không hợp lệ",
                    HttpStatus.INTERNAL_SERVER_ERROR
            ){};

    public static final ErrorCode INVALID_MAP_SEGMENT =
            new ErrorCode(
                    4302,
                    "Phân đoạn mapping cấu trúc tài khoản không hợp lệ",
                    HttpStatus.INTERNAL_SERVER_ERROR
            ){};
    public static final ErrorCode VALIDATION_ERROR = new ErrorCode(4000, "Dữ liệu đầu vào không chính xác", HttpStatus.UNPROCESSABLE_ENTITY) {};

    public static final ErrorCode ERROR_UPLOAD_FILE = new ErrorCode(4000, "Lỗi upload file lên hệ thống lưu trữ", HttpStatus.INTERNAL_SERVER_ERROR) {};

    public static final ErrorCode ERROR_BAL = new ErrorCode(4000, "Số tiền phát sinh đang vượt quá Số dư thực tế cho tài khoản", HttpStatus.INTERNAL_SERVER_ERROR) {};

    public static final ErrorCode INVALID_APPROVAL_STATUS = new ErrorCode(4002,
            "Trạng thái phê duyệt không hợp lệ: %s",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode BUSINESS_DATA_MISSING = new ErrorCode(4004, "Không tìm thấy dữ liệu nghiệp vụ",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode NOT_NULL= new ErrorCode(6001, "Giá trị không được để trống",
            HttpStatus.INTERNAL_SERVER_ERROR) {
    };

    public static final ErrorCode PROCESS_NOT_FOUND = new ErrorCode(4005, "Không tìm thấy hồ sơ process",
            HttpStatus.NOT_FOUND) {
    };
    public static final ErrorCode ERROR_DATA_EMPTY = new ErrorCode(4001, "Dữ liệu phiếu không được để trống", HttpStatus.BAD_REQUEST) {};

    // --- Thông tin chung ---
    public static final ErrorCode ERROR_VOUCHER_TYPE_REQ = new ErrorCode(4002, "Loại phiếu là bắt buộc", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode ERROR_TOTAL_AMT_REQ = new ErrorCode(4003, "Số tiền là bắt buộc", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode ERROR_CONTENT_REQ = new ErrorCode(4004, "Nội dung giao dịch là bắt buộc", HttpStatus.BAD_REQUEST) {};

    // --- Thông tin đối tượng [1] ---
    public static final ErrorCode ERROR_OBJ_TYPE_REQ = new ErrorCode(4005, "Loại đối tượng là bắt buộc", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode ERROR_OBJ_NAME_REQ = new ErrorCode(4006, "Tên người giao dịch là bắt buộc", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode ERROR_ID_REQ = new ErrorCode(4007, "Số CCCD là bắt buộc", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode ERROR_ISSUE_DATE_REQ = new ErrorCode(4008, "Ngày cấp là bắt buộc", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode ERROR_ISSUE_PLACE_REQ = new ErrorCode(4009, "Nơi cấp là bắt buộc", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode ERROR_ADDRESS_REQ = new ErrorCode(4010, "Địa chỉ là bắt buộc", HttpStatus.BAD_REQUEST) {};

    // --- Thông tin bút toán [2] ---
    public static final ErrorCode ERROR_EMPTY_ENTRIES = new ErrorCode(4011, "Danh sách hạch toán không được để trống", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode ERROR_ACC_CLASS_REQ = new ErrorCode(4012, "Mã phân loại là bắt buộc", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode ERROR_ACC_NO_REQ = new ErrorCode(4013, "Số tài khoản là bắt buộc", HttpStatus.BAD_REQUEST) {};

    // Validate Logic tiền
    public static final ErrorCode ERROR_AMT_MISMATCH = new ErrorCode(4014, "Số tiền hạch toán phải bằng tổng số tiền của phiếu", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode ERROR_FORMAT_FILE = new ErrorCode(4014, "Sai định dạng file cho mã", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode TEMPLATE_CODE_NOT_FOUND = new ErrorCode(4014, "Mã code không tồn tại", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode FILE_PATH_NOT_FOUND = new ErrorCode(4014, "Không tồn tại file path", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode ENTRY_ACCT_PROCESS_EXISTED = new ErrorCode(4014, "Cấu hình bút toán đã tồn tại!", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode ENTRY_DTL_NOT_EXIST = new ErrorCode(4015, "Cấu hình phát sinh chi tiết không tồn tại!", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode ENTRY_NOT_EXIST = new ErrorCode(4016, "Cấu hình phát sinh không tồn tại!", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode ACC_CLASS_COA_MAP_NOT_EXIST = new ErrorCode(4017, "Cấu hình thông tin phân loại nội bộ không tồn tại!", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode TXN_ACCT_ENTRY_NOT_EXIST = new ErrorCode(4018, "Thông tin giao dịch kế toán phát sinh không tồn tại!", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode TXN_ACCT_NOT_EXIST = new ErrorCode(4019, "Thông tin giao dịch kế toán không tồn tại!", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode TXN_ACCT_ENTRY_DTL_NOT_EXIST = new ErrorCode(4020, "Thông tin giao dịch kế toán phát sinh chi tiết không tồn tại!", HttpStatus.BAD_REQUEST) {};

    public static final ErrorCode VALID_ACC_NOO = new ErrorCode(4014, "Cấu trúc tài khoản này chỉ cho phép 1 bản ghi duy nhất và đã tồn tại.", HttpStatus.BAD_REQUEST) {};


}
