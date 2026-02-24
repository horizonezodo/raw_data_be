package ngvgroup.com.loan.core.constant;


import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class LoanErrorCode {
    private LoanErrorCode() {}

    public static final ErrorCode NOT_FOUND =
            new ErrorCode(404, "Không tìm thấy", HttpStatus.NOT_FOUND) {};

    public static final ErrorCode BAD_REQUEST =
            new ErrorCode(400, "Yêu cầu không hợp lệ", HttpStatus.BAD_REQUEST) {};

    public static final ErrorCode SCORE_VALUE_EXCEPTION =
            new ErrorCode(4098, "Điểm số phải nằm trong khoảng từ điểm cận dưới đến điểm cận trên", HttpStatus.BAD_REQUEST) {};

    public static final ErrorCode SCORE_VALUE_MAX_EXCEPTION =
            new ErrorCode(4099, "Điểm cận trên phải lớn hơn điểm số và điểm cận dưới", HttpStatus.BAD_REQUEST) {};

    public static final ErrorCode SCORE_VALUE_MIN_EXCEPTION =
            new ErrorCode(4100, "Điểm cận dưới phải nhỏ hơn điểm số và điểm cận trên", HttpStatus.BAD_REQUEST) {};

    public static final ErrorCode INTEREST_RATE_ALREADY_EXISTS =
            new ErrorCode(400, "Lãi suất đã tồn tại", HttpStatus.CONFLICT) {};

    public static final ErrorCode VALIDATION_ERROR =
            new ErrorCode(4000, "Dữ liệu đầu vào không chính xác %s", HttpStatus.UNPROCESSABLE_ENTITY) {};

    public static final ErrorCode SCORING_TYPE_IS_USE =
            new ErrorCode(400, "Chỉ tiêu đã được sử dụng %s", HttpStatus.CONFLICT) {};

    public static final ErrorCode MINIO_ERROR =
            new ErrorCode(400, "MinIo error %s", HttpStatus.INTERNAL_SERVER_ERROR) {};

    public static final ErrorCode SCORING_BENCH_MARK_ERROR =
            new ErrorCode(400, "Không tìm thấy điểm chuẩn với code: %s", HttpStatus.NOT_FOUND) {};

    public static final ErrorCode SCORING_INDC_GROUP_ALREADY_EXISTS =
            new ErrorCode(400, "Nhóm chỉ tiêu đã tồn tại", HttpStatus.CONFLICT) {};

    public static final ErrorCode SCORING_INDC_GROUP_IS_USE =
            new ErrorCode(400, "Nhóm chỉ tiêu đã được sử dụng", HttpStatus.CONFLICT) {};

    public static final ErrorCode SCORING_TYPE_ALREADY_EXISTS =
            new ErrorCode(400, "Loại xếp hạng tín dụng đã tồn tại", HttpStatus.CONFLICT) {};

    public static final ErrorCode SCORING_TYPE_IS_USER =
            new ErrorCode(400, "Loại xếp hạng tín dụng đã được sử dụng", HttpStatus.CONFLICT) {};

    public static final ErrorCode LOAN_PURPOSE_ALREADY_EXISTS =
            new ErrorCode(400, "Mục đích vay vốn đã tồn tại", HttpStatus.CONFLICT) {};

    public static final ErrorCode WRITE_EXCEL_ERROR
            = new ErrorCode(5002, "Lỗi ghi dữ liệu ra file excel %s ", HttpStatus.INTERNAL_SERVER_ERROR) {};

    public static final ErrorCode FILE_EMPTY =
            new ErrorCode(400, "File không được để trống", HttpStatus.BAD_REQUEST) {};

    public static final ErrorCode INVALID_TASK =
            new ErrorCode(404, "Thông tin task không hợp lệ", HttpStatus.BAD_REQUEST) {};

    public static final ErrorCode UPLOAD_FILE_FAILED =
            new ErrorCode(500, "Upload file thất bại", HttpStatus.INTERNAL_SERVER_ERROR) {};

    public static final ErrorCode INVALID_STATUS =
            new ErrorCode(411, "Trạng thái không hợp lệ", HttpStatus.BAD_REQUEST) {};

    public static final ErrorCode PRODUCT_ALREADY_EXISTS =
            new ErrorCode(400, "Sản phẩm đã tồn tại %s", HttpStatus.CONFLICT) {};

    public static final ErrorCode BUCKET_CREATED_FAILED =
            new ErrorCode(412, "Không tạo được bucket", HttpStatus.BAD_REQUEST) {};

    public static final ErrorCode EXPORT_DOCX_ERROR =
            new ErrorCode(413, "Lỗi xuất biểu mẫu", HttpStatus.BAD_REQUEST) {};

    public static final ErrorCode INVALID_APPROVAL_STATUS = new ErrorCode(4002,
            "Trạng thái phê duyệt không hợp lệ: %s",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode BUSINESS_DATA_MISSING = new ErrorCode(4004, "Không tìm thấy dữ liệu nghiệp vụ",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode CAPITAL_USE_RATE_SUM_INVALID = new ErrorCode(
            4101,
            "Tổng tỷ lệ phân bổ vốn không hợp lệ . Giá trị tỷ lệ phân bổ vốn hiện tại %s",
            HttpStatus.BAD_REQUEST
    ) {
    };

    public static final ErrorCode CAPITAL_USE_LEVEL_DUPLICATE = new ErrorCode(
            4101,
            "Không thể tồn tại 2 phần tử có cùng ngày hiệu lực, cùng loại tỉ lệ và cùng cấp hiệu lực",
            HttpStatus.BAD_REQUEST
    ) {
    };
}
