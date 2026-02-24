package ngvgroup.com.bpm.client.exception;

import org.springframework.http.HttpStatus;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;

/**
 * Error codes for BPM Client package.
 * This class provides error codes specific to the BPM client library.
 */
public class BpmClientErrorCode {

    private BpmClientErrorCode() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Error code for invalid JSON format.
     * Code: 4001
     * HTTP Status: BAD_REQUEST
     */
    public static final ErrorCode INVALID_JSON_FORMAT = new ErrorCode(
            4001, 
            "Định dạng JSON không hợp lệ: %s", 
            HttpStatus.BAD_REQUEST
    ) {};

    /**
     * Error code for invalid audit data.
     * Code: 4002
     * HTTP Status: BAD_REQUEST
     */
    public static final ErrorCode INVALID_AUDIT_DATA = new ErrorCode(
            4002,
            "Dữ liệu Audit không hợp lệ: %s",
            HttpStatus.BAD_REQUEST
    ) {};

    /**
     * Error code for resource mapping not found
     */
    public static final ErrorCode RESOURCE_MAPPING_NOT_FOUND = new ErrorCode(
            4003,
            "Người dùng không được phép thực hiện thao tác này",
            HttpStatus.BAD_REQUEST
    ) {};
}
