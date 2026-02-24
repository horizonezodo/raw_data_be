package com.ngvgroup.bpm.core.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ErrorCode implements IErrorCode {
    public static final ErrorCode OK = new ErrorCode(200, "Thành công", HttpStatus.OK) {};
    public static final ErrorCode CREATED = new ErrorCode(201, "Đã tạo", HttpStatus.CREATED) {};
    public static final ErrorCode ACCEPTED = new ErrorCode(202, "Đã chấp nhận", HttpStatus.ACCEPTED) {};
    public static final ErrorCode NO_CONTENT = new ErrorCode(204, "Không có nội dung", HttpStatus.NO_CONTENT) {};

    public static final ErrorCode BAD_REQUEST = new ErrorCode(400, "Yêu cầu không hợp lệ", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode UNAUTHORIZED = new ErrorCode(401, "Không có quyền truy cập", HttpStatus.UNAUTHORIZED) {};
    public static final ErrorCode FORBIDDEN = new ErrorCode(403, "Bị từ chối truy cập", HttpStatus.FORBIDDEN) {};
    public static final ErrorCode NOT_FOUND = new ErrorCode(404, "Không tìm thấy", HttpStatus.NOT_FOUND) {};
    public static final ErrorCode METHOD_NOT_ALLOWED = new ErrorCode(405, "Phương thức không được hỗ trợ", HttpStatus.METHOD_NOT_ALLOWED) {};
    public static final ErrorCode NOT_ACCEPTABLE = new ErrorCode(406, "Không chấp nhận", HttpStatus.NOT_ACCEPTABLE) {};
    public static final ErrorCode REQUEST_TIMEOUT = new ErrorCode(408, "Yêu cầu hết hạn", HttpStatus.REQUEST_TIMEOUT) {};
    public static final ErrorCode CONFLICT = new ErrorCode(409, "Dữ liệu đã tồn tại", HttpStatus.CONFLICT) {};
    public static final ErrorCode GONE = new ErrorCode(410, "Đã xóa", HttpStatus.GONE) {};
    public static final ErrorCode LENGTH_REQUIRED = new ErrorCode(411, "Yêu cầu độ dài", HttpStatus.LENGTH_REQUIRED) {};
    public static final ErrorCode PAYLOAD_TOO_LARGE = new ErrorCode(413, "Dữ liệu quá lớn", HttpStatus.PAYLOAD_TOO_LARGE) {};
    public static final ErrorCode URI_TOO_LONG = new ErrorCode(414, "URI quá dài", HttpStatus.URI_TOO_LONG) {};
    public static final ErrorCode UNSUPPORTED_MEDIA_TYPE = new ErrorCode(415, "Loại phương tiện không được hỗ trợ", HttpStatus.UNSUPPORTED_MEDIA_TYPE) {};
    public static final ErrorCode REQUESTED_RANGE_NOT_SATISFIABLE = new ErrorCode(416, "Phạm vi yêu cầu không thỏa mãn", HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE) {};
    public static final ErrorCode UNPROCESSABLE_ENTITY = new ErrorCode(422, "Dữ liệu không hợp lệ", HttpStatus.UNPROCESSABLE_ENTITY) {};
    public static final ErrorCode LOCKED = new ErrorCode(423, "Bị khóa", HttpStatus.LOCKED) {};
    public static final ErrorCode UPGRADE_REQUIRED = new ErrorCode(426, "Yêu cầu nâng cấp", HttpStatus.UPGRADE_REQUIRED) {};
    public static final ErrorCode TOO_MANY_REQUESTS = new ErrorCode(429, "Quá nhiều yêu cầu", HttpStatus.TOO_MANY_REQUESTS) {};
    public static final ErrorCode REQUEST_HEADER_FIELDS_TOO_LARGE = new ErrorCode(431, "Trường tiêu đề yêu cầu quá lớn", HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE) {};

    public static final ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode(500, "Lỗi chưa xác định", HttpStatus.INTERNAL_SERVER_ERROR) {};
    public static final ErrorCode SERVICE_UNAVAILABLE = new ErrorCode(503, "Dịch vụ không khả dụng", HttpStatus.SERVICE_UNAVAILABLE) {};
    public static final ErrorCode GATEWAY_TIMEOUT = new ErrorCode(504, "Cổng hết thời gian chờ", HttpStatus.GATEWAY_TIMEOUT) {};
    public static final ErrorCode HTTP_VERSION_NOT_SUPPORTED = new ErrorCode(505, "Phiên bản HTTP không được hỗ trợ", HttpStatus.HTTP_VERSION_NOT_SUPPORTED) {};
    public static final ErrorCode INSUFFICIENT_STORAGE = new ErrorCode(507, "Bộ nhớ không đủ", HttpStatus.INSUFFICIENT_STORAGE) {};
    public static final ErrorCode BANDWIDTH_LIMIT_EXCEEDED = new ErrorCode(509, "Vượt quá giới hạn băng thông", HttpStatus.BANDWIDTH_LIMIT_EXCEEDED) {};

    // JWT Authentication Error Code
    public static final ErrorCode JWT_AUTHENTICATION_FAILED = new ErrorCode(401, "Token không hợp lệ", HttpStatus.UNAUTHORIZED) {};

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;

    protected ErrorCode(Integer code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}