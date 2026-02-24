package com.naas.category_service.exception;

import com.ngvgroup.bpm.core.exception.ErrorCode;
import org.springframework.http.HttpStatus;


public class CategoryErrorCode {
    public static final ErrorCode CATEGORY_ALREADY_EXISTS =
            new ErrorCode(409, "Danh mục đã tồn tại", HttpStatus.CONFLICT) {};
    public static final ErrorCode CATEGORY_HAS_CHILDREN =
            new ErrorCode(400, "Không thể xóa danh mục có danh mục con", HttpStatus.BAD_REQUEST) {};
    public static final ErrorCode COUNTRY_ALREADY_EXISTS =
            new ErrorCode(400, "Quốc gia đã tồn tại", HttpStatus.CONFLICT) {};
    public static final ErrorCode PROVINCE_ALREADY_EXISTS =
            new ErrorCode(400, "Tỉnh/thành phố đã tồn tại", HttpStatus.CONFLICT) {};
    public static final ErrorCode DISTRICT_ALREADY_EXISTS =
            new ErrorCode(400, "Quận/huyện đã tồn tại", HttpStatus.CONFLICT) {};
    public static final ErrorCode WARD_ALREADY_EXISTS =
            new ErrorCode(400, "Phường/xã đã tồn tại", HttpStatus.CONFLICT) {};
    public static final ErrorCode INSTITUTION_ALREADY_EXISTS =
            new ErrorCode(400, "Cơ sở đã tồn tại", HttpStatus.CONFLICT) {};
    public static final ErrorCode POSITION_IS_USE =
            new ErrorCode(400, "Chức vụ đang được sử dụng", HttpStatus.CONFLICT) {};
    public static final ErrorCode ECONOMIC_TYPE_ALREADY_EXISTS =
            new ErrorCode(400, "Loại kinh tế đã tồn tại", HttpStatus.CONFLICT) {};
    public static final ErrorCode INDUSTRY_ALREADY_EXISTS =
            new ErrorCode(400, "Ngành kinh tế đã tồn tại", HttpStatus.CONFLICT) {};
    public static final ErrorCode LOAN_PURPOSE_ALREADY_EXISTS =
            new ErrorCode(400, "Mục đích vay vốn đã tồn tại", HttpStatus.CONFLICT) {};
    public static final ErrorCode SCORING_INDC_GROUP_ALREADY_EXISTS =
            new ErrorCode(400, "Nhóm chỉ tiêu đã tồn tại", HttpStatus.CONFLICT) {};

    public static final ErrorCode SCORING_INDC_GROUP_IS_USE =
            new ErrorCode(400, "Nhóm chỉ tiêu đã được sử dụng", HttpStatus.CONFLICT) {};
    public static final ErrorCode INTEREST_RATE_ALREADY_EXISTS =
            new ErrorCode(400, "Lãi suất đã tồn tại", HttpStatus.CONFLICT) {};
    public static final ErrorCode INTEREST_RATE_DTL_ALREADY_EXISTS =
            new ErrorCode(400, "Thông tin lãi suất đã tồn tại", HttpStatus.CONFLICT) {};
    public static final ErrorCode SCORING_TYPE_ALREADY_EXISTS =
            new ErrorCode(400, "Loại xếp hạng tín dụng đã tồn tại", HttpStatus.CONFLICT) {};

    public static final ErrorCode SCORING_TYPE_IS_USER =
            new ErrorCode(400, "Loại xếp hạng tín dụng đã được sử dụng", HttpStatus.CONFLICT) {};
}
