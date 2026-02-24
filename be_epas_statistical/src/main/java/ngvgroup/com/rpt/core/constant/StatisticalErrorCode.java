package ngvgroup.com.rpt.core.constant;

import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class StatisticalErrorCode {
    private StatisticalErrorCode() {
    }

    public static final ErrorCode TEMPLATE_NOT_FOUND = new ErrorCode(4014, "Không tìm thấy Template code",
            HttpStatus.NOT_FOUND) {
    };
    public static final ErrorCode UPLOAD_TEMPLATE_FILE_FALL = new ErrorCode(4015,
            "Upload file template không thành công ", HttpStatus.INTERNAL_SERVER_ERROR) {
    };
    public static final ErrorCode TEMPLATE_IS_NOT_EXISTS = new ErrorCode(4016, "Mã template đã tồn tại trong hệ thống ",
            HttpStatus.CONFLICT) {
    };

    public static final ErrorCode NOT_EXISTS = new ErrorCode(4016, "Đã tồn tại trong hệ thống ",
            HttpStatus.CONFLICT) {
    };

    public static final ErrorCode TYPE_KPI_NOT_FOUND = new ErrorCode(4023, "Không tìm thấy loại chỉ tiêu ",
            HttpStatus.NOT_FOUND) {
    };
    public static final ErrorCode TYPE_KPI_CONFLICT = new ErrorCode(4024, "KPI TYPE CODE đã tồn tại trong hệ thống",
            HttpStatus.CONFLICT) {
    };
    public static final ErrorCode BAD_REQUEST = new ErrorCode(400, "Yêu cầu không hợp lệ", HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode NOT_EMPTY = new ErrorCode(4001, "Không được để trống", HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode AI_TYPE_NOT_FOUND = new ErrorCode(4001, "Không tìm thấy AI Type",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode AI_TOOL_NOT_FOUND = new ErrorCode(4001, "Không tìm thấy công cu AI",
            HttpStatus.BAD_REQUEST) {
    };

    // Validate ERROR
    public static final ErrorCode TEMPLATE_SHEET_DTO_IS_EMPTY = new ErrorCode(4017, "Danh sách sheet nhận từ FE trống ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TEMPLATE_KPI_DTO_IS_EMPTY = new ErrorCode(4018, "Danh sách KPI nhận từ FE trống ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TEMPLATE_DEAD_LINE_DTO_IS_EMPTY = new ErrorCode(4018, "Danh sách Deadline nhận từ FE trống ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TEMPLATE_DTO_IS_EMPTY = new ErrorCode(4019,
            "Thông tin nhận mẫu biểu nhận từ FE trống ", HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TEMPLATE_CODE_INVALID = new ErrorCode(4020, "Mã mẫu biểu không hợp lệ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TEMPLATE_NAME_INVALID = new ErrorCode(4021, "Tên mẫu biểu không hợp lệ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode REGULATORY_CODE_INVALID = new ErrorCode(4021, "Mã loại quy định nội bộ không hợp lệ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode REGULATORY_NAME_INVALID = new ErrorCode(4021, "Tên loại quy định nội bộ không hợp lệ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TEMPLATE_GROUP_CODE_INVALID = new ErrorCode(4022, "Mã nhóm mẫu biểu không hợp lệ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TYPE_KPI_TYPE_IS_EMPTY = new ErrorCode(4018, "TYPE KPI nhận từ FE trống ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TEMPLATE_FREQUENCY_IS_EMPTY = new ErrorCode(4023, "Định kỳ không được để trống ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TEMPLATE_TIME_INVALID = new ErrorCode(4024,
            "Thời gian hết hiệu lực phải lớn hơn thời gian hiệu lực ", HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode AI_TOOL_DTO_IS_EMPTY = new ErrorCode(4024, "Công cụ AI không được để trống",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TOOL_AI_CODE_INVALID = new ErrorCode(4024, "Mã công cụ AI không được đẻ trống ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode IS_ACTIVE_INVALID = new ErrorCode(4024, "Trạng thái không hợp lệ ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TOOL_AI_NAME_INVALID = new ErrorCode(4024, "Tên công cụ AI không được để trống ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TOOL_AI_TYPE_INVALID = new ErrorCode(4024, "Mã loại công cụ AI không được để trống ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode REQUIRES_AUTH_INVALID = new ErrorCode(4024, "Yêu cầu xác thực không được để trống ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode RETRY_COUNT_INVALID = new ErrorCode(4024, "Số lần chạy lại không được để trống ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TIMOUT_SECONDS_INVALID = new ErrorCode(4024,
            "Thời gian chờ tối đa không được để trống ", HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode AI_TOOL_CODE_CONFLIG = new ErrorCode(4024, "Mã công cụ AI đã tồn tại trong hệ thống ",
            HttpStatus.CONFLICT) {
    };
    public static final ErrorCode OUTPUT_FORMAT_INVALID = new ErrorCode(4024, "Định dạng đầu ra không hợp lệ ",
            HttpStatus.CONFLICT) {
    };
    public static final ErrorCode TOOL_AI_TYPE_DTO_IS_EMPTY = new ErrorCode(4040, "Loại công cụ AI không được để trống",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TOOL_AI_TYPE_NAME_INVALID = new ErrorCode(4024,
            "Tên loại công cụ AI không được để trống ", HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TOOL_AI_TYPE_CONFIG = new ErrorCode(4024, "Tên loại công cụ AI bị trùng ",
            HttpStatus.CONFLICT) {
    };

    public static final ErrorCode STAT_KPI_CONFLICT = new ErrorCode(4024, "Mã chỉ tiêu đang là mã công thức cha của",
            HttpStatus.CONFLICT) {
    };

    public static final ErrorCode OVERLAP_SCORE_RANGE = new ErrorCode(4024,
            "Khoảng điểm cận trên và cận dưới bị chồng lấn với bản ghi khác", HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode MIN_GREATER_THAN_MAX = new ErrorCode(4024,
            "Giá trị điểm cận dưới không được lớn hơn điểm cận trên", HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode DUPLICATE_BENCHMARK_CODE = new ErrorCode(4024,
            "Mã xếp loại đã tồn tại trong hệ thống", HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode DUPLICATE_SCORE_GROUP_CODE = new ErrorCode(4024,
            "Mã nhóm chỉ tiêu đã tồn tại trong hệ thống", HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode SCORE_DTO_IS_NULL = new ErrorCode(4024, "Chấm điểm xếp hạng không dược để trống  ",
            HttpStatus.CONFLICT) {
    };

    // Status Configuration Error Codes
    public static final ErrorCode STATUS_NOT_FOUND = new ErrorCode(4050, "Không tìm thấy trạng thái với mã",
            HttpStatus.NOT_FOUND) {
    };
    public static final ErrorCode STATUS_CODE_CONFLICT = new ErrorCode(4051, "Mã trạng thái đã tồn tại trong hệ thống",
            HttpStatus.CONFLICT) {
    };
    public static final ErrorCode STATUS_DTO_IS_EMPTY = new ErrorCode(4052, "Thông tin trạng thái không được để trống",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode STATUS_CODE_INVALID = new ErrorCode(4053, "Mã trạng thái không hợp lệ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode STATUS_NAME_INVALID = new ErrorCode(4054, "Tên trạng thái không được để trống",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode SORT_NUMBER_INVALID = new ErrorCode(4055, "Thứ tự hiển thị không hợp lệ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode EXPORT_EXCEL_FAILED = new ErrorCode(4056, "Lỗi khi xuất file Excel",
            HttpStatus.INTERNAL_SERVER_ERROR) {
    };

    // Workflow Configuration Error Codes
    public static final ErrorCode WORKFLOW_NOT_FOUND = new ErrorCode(4060, "Không tìm thấy quy trình với mã",
            HttpStatus.NOT_FOUND) {
    };
    public static final ErrorCode WORKFLOW_CODE_CONFLICT = new ErrorCode(4061, "Mã quy trình đã tồn tại trong hệ thống",
            HttpStatus.CONFLICT) {
    };
    public static final ErrorCode WORKFLOW_DTO_IS_EMPTY = new ErrorCode(4062, "Thông tin quy trình không được để trống",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode WORKFLOW_CODE_INVALID = new ErrorCode(4063, "Mã quy trình không hợp lệ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode WORKFLOW_NAME_INVALID = new ErrorCode(4064, "Tên quy trình không được để trống",
            HttpStatus.BAD_REQUEST) {
    };

    // Workflow Transition Error Codes
    public static final ErrorCode TRANSITION_NOT_FOUND = new ErrorCode(4070, "Không tìm thấy hành động với mã",
            HttpStatus.NOT_FOUND) {
    };
    public static final ErrorCode TRANSITION_CODE_CONFLICT = new ErrorCode(4071,
            "Mã hành động đã tồn tại trong hệ thống", HttpStatus.CONFLICT) {
    };
    public static final ErrorCode TRANSITION_DTO_IS_EMPTY = new ErrorCode(4072,
            "Thông tin hành động không được để trống", HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TRANSITION_CODE_INVALID = new ErrorCode(4073, "Mã hành động không hợp lệ",
            HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TRANSITION_NAME_INVALID = new ErrorCode(4074, "Tên hành động không được để trống",
            HttpStatus.BAD_REQUEST) {
    };

    // Transition Condition Error Codes
    public static final ErrorCode TRANSITION_COND_NOT_FOUND = new ErrorCode(4080, "Không tìm thấy điều kiện hành động",
            HttpStatus.NOT_FOUND) {
    };
    public static final ErrorCode TRANSITION_COND_DTO_IS_EMPTY = new ErrorCode(4081,
            "Thông tin điều kiện không được để trống", HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TRANSITION_COND_TYPE_INVALID = new ErrorCode(4082, "Loại điều kiện không hợp lệ",
            HttpStatus.BAD_REQUEST) {
    };

    // Transition Post Function Error Codes
    public static final ErrorCode TRANSITION_POST_FUNC_NOT_FOUND = new ErrorCode(4090,
            "Không tìm thấy hậu xử lý chuyển trạng thái", HttpStatus.NOT_FOUND) {
    };
    public static final ErrorCode TRANSITION_POST_FUNC_DTO_IS_EMPTY = new ErrorCode(4091,
            "Thông tin hậu xử lý không được để trống", HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode TRANSITION_POST_FUNC_TYPE_INVALID = new ErrorCode(4092, "Loại hậu xử lý không hợp lệ",
            HttpStatus.BAD_REQUEST) {
    };


    //Template Dead line Error Code
    public static final ErrorCode FREQUENCE_IS_EMPTY = new ErrorCode(4093, "Định kỳ không được để trống",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode DEADLINE_TYPE_IS_EMPTY = new ErrorCode(4094, "Loại tính dead line không được để trống",
            HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode REPORT_GROUP_CODE_ALREADY_EXISTS =
            new ErrorCode(409, "Mã nhóm báo cáo đã tồn tại trong báo cáo", HttpStatus.CONFLICT) {
            };

    public static final ErrorCode SOURCE_PARAM_CODE_ALREADY_EXISTS =
            new ErrorCode(409, "Tham số nguồn đã tồn tại trong điều kiện ràng buộc tham số", HttpStatus.CONFLICT) {
            };

    public static final ErrorCode TARGET_PARAM_CODE_ALREADY_EXISTS =
            new ErrorCode(409, "Tham số đích đã tồn tại trong điều kiện ràng buộc tham số", HttpStatus.CONFLICT) {
            };

    public static final ErrorCode DUPLICATE_RULE_TYPE_CODE = new ErrorCode(4095,
            "Mã loại quy tắc ", HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode DUPLICATE_RULE_CODE = new ErrorCode(4096,
            "Mã quy tắc ", HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode NOT_FOUND = new ErrorCode(4096,
            "Không tìm thấy %s ", HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode IN_VALID_USERNAME = new ErrorCode(4096,
            "Username is null or empty", HttpStatus.INTERNAL_SERVER_ERROR) {
    };

    public static final ErrorCode IN_VALID_PASSWORD = new ErrorCode(4096,
            "Password is null or empty", HttpStatus.INTERNAL_SERVER_ERROR) {
    };

    public static final ErrorCode AUTHENTICATION_RESPONSE_NULL = new ErrorCode(4096,
            "Authentication response is null or empty", HttpStatus.INTERNAL_SERVER_ERROR) {
    };

    public static final ErrorCode API_ERROR = new ErrorCode(4096,
            "API error: %s", HttpStatus.INTERNAL_SERVER_ERROR) {
    };

    public static final ErrorCode INVALID_RESPONSE = new ErrorCode(4096,
            "Invalid response format, data not found", HttpStatus.INTERNAL_SERVER_ERROR) {
    };

    public static final ErrorCode INVALID_RESPONSE_ACCESS_TOKEN = new ErrorCode(4096,
            "Invalid response format, access_token not found in data", HttpStatus.INTERNAL_SERVER_ERROR) {
    };

    public static final ErrorCode ACCESS_TOKEN_IS_NULL_OR_EMPTY = new ErrorCode(4096,
            "Access token is null or empty in response", HttpStatus.INTERNAL_SERVER_ERROR) {
    };

    public static final ErrorCode AUTHENTICATE_FAILED = new ErrorCode(4096,
            "Failed to authenticate %s", HttpStatus.INTERNAL_SERVER_ERROR) {
    };

    public static final ErrorCode AUTHENTICATE_MODEL_FAILED = new ErrorCode(4096,
            "Failed to get authenticate model", HttpStatus.INTERNAL_SERVER_ERROR) {
    };

    public static final ErrorCode EMPTY_TEMPLATE_FILE = new ErrorCode(4096,
            "Empty TEMPLATE_FILE for", HttpStatus.INTERNAL_SERVER_ERROR) {
    };

    public static final ErrorCode INVALID_SHEET_CONFIG = new ErrorCode(4096,
            "No sheet config for template", HttpStatus.INTERNAL_SERVER_ERROR) {
    };

    public static final ErrorCode CANNOT_AGGREGATE_DATA = new ErrorCode(4097,
            "Báo cáo với định kỳ ngày dữ liệu trên đã được tổng hợp", HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode WORKFLOW_CODE_DIFFERENT = new ErrorCode(4098,
            "Các bản ghi được chọn không cùng Workflow hoặc cùng Trạng thái!", HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode BAD_READ_FILE = new ErrorCode(4099,
            "Không thể đọc được file trong cơ sở dữ liệu!", HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode VALID_STAT_TYPE_CODE = new ErrorCode(4099,
            "Mã loại Tk đã tồn tại.", HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode VALID_STAT_REGULATORY_CODE = new ErrorCode(4099,
            "Mã Tk đã tồn tại.", HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode STAT_TYPE_EXPORT_ERR = new ErrorCode(4099,
            "Xuất Excel thất bại.", HttpStatus.BAD_REQUEST) {
    };

    public static final ErrorCode POWER_BI_CLOUD_REPORT_SOURCE_INVALID = new ErrorCode(4099,
            "Đường dẫn báo cáo không đúng định dạng", HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode POWER_BI_CLOUD_REPORT_SOURCE_NOTFOUND = new ErrorCode(4099,
            "Đường dẫn báo cáo không được để trống", HttpStatus.BAD_REQUEST) {
    };
    public static final ErrorCode POWER_BI_CLOUD_AUTH_FAILED = new ErrorCode(4100,
            "Không thể xác thực với Azure/Power BI", HttpStatus.UNAUTHORIZED) {
    };
}
