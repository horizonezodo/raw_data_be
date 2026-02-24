package ngvgroup.com.ibm.core.constant;


import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class IbmErrorCode {
    private IbmErrorCode(){}

    public static final ErrorCode DUPLICATE_INTEREST_RATE_CODE= new ErrorCode(5000, "Mã lãi suất %s đã tồn tại trong hệ thống",
            HttpStatus.CONFLICT) {
    };
}
