package com.naas.admin_service.core.contants;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import com.ngvgroup.bpm.core.common.exception.ErrorCode;

@Getter
public class CommonErrorCode {
    private CommonErrorCode() {
    }

    public static final ErrorCode BAD_REQUEST = new ErrorCode(400, "Yêu cầu không hợp lệ", HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode(500, "Lỗi hệ thống",
            HttpStatus.INTERNAL_SERVER_ERROR) {
    };
    public static final ErrorCode EXISTS = new ErrorCode(409, "Đã tồn tại", HttpStatus.CONFLICT) {
    };
    public static final ErrorCode INVALID_DATA_ENTITY = new ErrorCode(422, "Dữ liệu không hợp lệ",
            HttpStatus.UNPROCESSABLE_ENTITY) {
    };
    public static final ErrorCode ADD_CLIENT_FAILURE = new ErrorCode(700, "Thêm mới client thất bại",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode NOT_FOUND = new ErrorCode(404, "Không tìm thấy", HttpStatus.NOT_FOUND) {
    };

    // "Authentication failed",
    public static final ErrorCode INVALID_CREDENTIAL = new ErrorCode(701, "Invalid client credentials",
            HttpStatus.INTERNAL_SERVER_ERROR) {
    };
    public static final ErrorCode USERNAME_PASSWORD_INCORECT = new ErrorCode(702, "User name or password incorrect",
            HttpStatus.INTERNAL_SERVER_ERROR) {
    };
    public static final ErrorCode REFRESH_TOKEN_INVALID = new ErrorCode(703, "Refresh token invalid",
            HttpStatus.INTERNAL_SERVER_ERROR) {
    };

    public static final ErrorCode RESOURCE_TYPE_ALREADY_EXISTS = new ErrorCode(4097, "Loại tài nguyên đã tồn tại", HttpStatus.CONFLICT) {};
    public static final ErrorCode POSITION_IS_USE =
            new ErrorCode(400, "Chức vụ đang được sử dụng", HttpStatus.CONFLICT) {};

    public static final ErrorCode ERROR_DAY_OF_WEEK = new ErrorCode(333, "DAY_OF_WEEK phải từ 2–8 (2=Thứ 2, ... 8=Chủ nhật)", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode ERROR_DATE_TIME = new ErrorCode(333, "không đúng định dạng HH:mm", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode INVALID_WORKING_HOUR = new ErrorCode(333, "Dữ liệu đang trống", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode EXIST_HOLIDAY_DATE = new ErrorCode(401, "Ngày nghỉ lễ %s đã tồn tại trong hệ thống!", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode EXIST_POSITION_ID = new ErrorCode(402, "Mã chức vụ %s đã tồn tại trong hệ thống!", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode EXIST_CREDIT_INST_ID = new ErrorCode(403, "Mã tổ chức tín dụng %s đã tồn tại trong hệ thống!", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode NOT_EXIST_HOLIDAY_DATE = new ErrorCode(404, "Ngày nghỉ lễ %s không tồn tại trong hệ thống!", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode NOT_HOLIDAY_DATE = new ErrorCode(405, "Ngày nghỉ lễ %s đã được đăng ký làm việc. Vui lòng hủy đăng ký tại chức năng Ngày làm việc trước khi thực hiện xóa ngày nghỉ này!", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode NOT_EXIST_WORKING_DATE = new ErrorCode(405, "Ngày làm việc %s không tồn tại!", HttpStatus.BAD_REQUEST) {};

    public static final ErrorCode CAPTCHA_INVALID = new ErrorCode(1001, "Mã Captcha không hợp lệ", HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode EMAIL_CONFIG_NOT_FOUND = new ErrorCode(406, "Không tìm thấy thông tin cấu hình mail gửi với id: ", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode INVALID_WORKING_DATE = new ErrorCode(333, "Ngày làm việc không hợp lệ.", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode SMALLER_WORKING_DATE_REGISTER = new ErrorCode(333, "Chỉ được đăng ký ngày lớn hơn ngày hiện tại.", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode INVALID_WORKING_DATE_REGISTER = new ErrorCode(333, "Thông tin đăng ký không hợp lệ.", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode WORKING_DATE_NOT_FOUND = new ErrorCode(333, "Không tìm thấy ngày làm việc để hủy.",  HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode INVALID_INIT_YEAR_REQUEST = new ErrorCode(333, "Yêu cầu đăng ký lịch làm việc năm không hợp lệ.",  HttpStatus.BAD_REQUEST) {};

    public static final ErrorCode NOT_EXIST_TEMPLATE_CODE = new ErrorCode(405, "Mã mẫu biểu %s không tồn tại!", HttpStatus.BAD_REQUEST) {};

    public static final ErrorCode VALID_HOUR = new ErrorCode(405, "Đã tồn tại giờ làm việc này trong hệ thống.", HttpStatus.BAD_REQUEST) {};

    public static final ErrorCode KEYCLOAK_MIN_LOWER_CASE = new ErrorCode(400, "Mật khẩu phải chứa ít nhất một ký tự viết thường (a-z).", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode KEYCLOAK_MIN_UPPER_CASE = new ErrorCode(400, "Mật khẩu phải chứa ít nhất một ký tự viết hoa (A-Z).", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode KEYCLOAK_MIN_DIGITS = new ErrorCode(400, "Mật khẩu phải chứa ít nhất một chữ số (0-9).", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode KEYCLOAK_MIN_SPECIAL_CHARS = new ErrorCode(400, "Mật khẩu phải chứa ký tự đặc biệt (ví dụ: @, #, !, ...).", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode KEYCLOAK_MIN_LENGTH = new ErrorCode(400, "Mật khẩu quá ngắn (độ dài ít nhất %s kí tự).", HttpStatus.BAD_REQUEST) {};

    public static final ErrorCode KEYCLOAK_UNKNOWN_ERROR = new ErrorCode(400, "Lỗi xác thực.", HttpStatus.BAD_REQUEST) {};

    public static final ErrorCode ECONOMIC_TYPE_ALREADY_EXISTS =
            new ErrorCode(400, "Loại kinh tế đã tồn tại", HttpStatus.CONFLICT) {};

    public static final ErrorCode INDUSTRY_ALREADY_EXISTS =
            new ErrorCode(400, "Ngành kinh tế đã tồn tại", HttpStatus.CONFLICT) {};

    public static final ErrorCode WARD_ALREADY_EXISTS = new ErrorCode(400, "Phường/xã đã tồn tại",
            HttpStatus.CONFLICT) {
    };
    public static final ErrorCode PROVINCE_ALREADY_EXISTS = new ErrorCode(400, "Tỉnh/thành phố đã tồn tại",
            HttpStatus.CONFLICT) {
    };
    public static final ErrorCode MENU_ID_ALREADY_EXISTS = new ErrorCode(400, "Menu ID %s đã tồn tại trong hệ thống",
            HttpStatus.CONFLICT) {
    };
    public static final ErrorCode NOT_EXIST_SETTING_CODE = new ErrorCode(405, "Mã setting %s không tồn tại!", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode MENU_NOT_FOUND = new ErrorCode(406, "Không tìm thấy Menu với id %s!", HttpStatus.BAD_REQUEST) {};

    public static final ErrorCode ERROR_MINIO = new ErrorCode(406, "Lỗi MinIO: ", HttpStatus.INTERNAL_SERVER_ERROR) {};

    public static final ErrorCode NOT_FOUND_FILE = new ErrorCode(406, "Không tìm thấy file trong hệ thống.", HttpStatus.INTERNAL_SERVER_ERROR) {};

    public static final ErrorCode ERROR_DOWNLOAD_FILE = new ErrorCode(406, "Không thể tải file từ hệ thống.", HttpStatus.INTERNAL_SERVER_ERROR) {};
    public static final ErrorCode WRITE_EXCEL_ERROR
            = new ErrorCode(5002, "Lỗi ghi dữ liệu ra file excel %s ", HttpStatus.INTERNAL_SERVER_ERROR) {};
    public static final ErrorCode TENANT_INACTIVE =
            new ErrorCode(4103, "Tenant %s không tồn tại hoặc đã bị disable", HttpStatus.FORBIDDEN) {};

}
