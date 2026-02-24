package ngvgroup.com.rpt.core.constant;

public class ExcelColumns {
    // Response Define
    public static final String RSD_RESPONSE_CODE = "Mã code phản hồi";
    public static final String RSD_RESPONSE_NAME = "Tên code phản hồi";
    public static final String RSD_DESCRIPTION = "Mô tả";
    
    // Rule Define
    public static final String RLD_RULE_CODE = "Mã Rule nghiệp vụ";
    public static final String RLD_RULE_NAME = "Tên Rule nghiệp vụ";
    public static final String RLD_RULE_TYPE_NAME = "Tên loại quy tắc";
    public static final String RLD_DESCRIPTION = "Mô tả";
    
    // Rule Type
    public static final String RLT_RULE_TYPE_CODE = "Mã loại quy tắc";
    public static final String RLT_RULE_TYPE_NAME = "Tên loại quy tắc";
    public static final String RLT_DESCRIPTION = "Mô tả";
    
    // Branch Result
    public static final String BR_CI_BR_NAME = "Tên chi nhánh";
    public static final String BR_CI_BR_CODE = "Mã chi nhánh";
    public static final String BR_ACHIEVED_SCORE = "Tổng điểm";
    public static final String BR_RANK_VALUE = "Phân loại rủi ro";
    public static final String RANK_CONTENT = "Mô tả";
    
    // Smr Score Search
    public static final String SSS_CI_NAME = "Đơn vị chấm điểm";
    public static final String SSS_TXN_DATE = "Ngày chấm điểm";
    public static final String SSS_MAKER_USER_NAME = "Người thực hiện";
    public static final String SSS_SCORE_INSTANCE_CODE = "Mã chấm điểm";
    
    // Smr Txn Score
    public static final String STS_SCORE_INSTANCE_CODE = "Mã chấm điểm";
    public static final String STS_SCORE_DATE = "Ngày chấm điểm";
    public static final String STS_MAKER_USER_NAME = "Người thực hiện";
    public static final String STS_CURRENT_STATUS_NAME = "Trạng thái";
    public static final String STS_CI_NAME = "Đơn vị chấm điểm";

    // Transaction Report
    public static final String TR_TT = "TT";
    public static final String TR_STATUS = "Trạng thái";
    public static final String TR_TXN_DATE = "Ngày GD";
    public static final String TR_STAT_INSTANCE_CODE = "Mã giao dịch";
    public static final String TR_TEMPLATE_CODE = "Mã báo cáo";
    public static final String TR_FREQUENCY = "Định kỳ";
    public static final String TR_REPORT_DATA_DATE = "Ngày dữ liệu";
    public static final String TR_REPORT_DUE_TIME = "Ngày hêt hạn";
    public static final String TR_CIRCULAR_NAME = "Quy định";
    public static final String TR_SLA_STATUS = "Cảnh báo SLA";
    public static final String TR_EXPORT_COUNT = "Số lần xuất";

    // Report Rule
    public static final String RR_CIRCULAR_NAME = "Quy định";
    public static final String RR_TEMPLATE_CODE = "Mã mẫu biểu";
    public static final String RR_TEMPLATE_NAME = "Tên mẫu biểu";
    public static final String RR_FREQUENCY = "Định kỳ";
    public static final String RR_STATUS_NAME = "Trạng thái";

    // Stat-type
    public static final String STAT_TYPE_CODE = "Mã loại TK";
    public static final String STAT_TYPE_NAME = "Tên loại TK";
    public static final String REPORT_MODULE_CODE = "Mã nhóm TK";
    public static final String REPORT_MODULE_NAME = "Tên nhóm TK";



    private ExcelColumns() {
        // Private constructor to prevent instantiation
    }
}
