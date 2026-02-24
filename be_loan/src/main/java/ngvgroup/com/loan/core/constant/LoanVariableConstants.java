package ngvgroup.com.loan.core.constant;

public class LoanVariableConstants {
    // ==================== Camunda Process Keys ====================
    public static final String PREFIX_INTEREST_REGISTER = "LNM.100.01";
    public static final String PREFIX_INTEREST_EDIT = "LNM.101.01";


    // ==================== Mail Parameter Keys ====================
    public static final String MAIL_PARAM_PROCESS_INSTANCE_CODE = "PROCESS_INTANCE_CODE";
    public static final String MAIL_PARAM_KS_HETO = "KS_HETO";
    public static final String MAIL_PARAM_COMMENT = "COMMENT";

    // ==================== External Task Topics ====================
    // =========== Interest ===============
    // Register
    public static final String LOAN_EXTERNAL_TASK_APPROVE = "LOAN_EXTERNAL_TASK_APPROVE";
    public static final String LOAN_EXTERNAL_TASK_EDIT = "LOAN_EXTERNAL_TASK_EDIT";
    public static final String LOAN_EXTERNAL_TASK_END_PROCESS = "LOAN_EXTERNAL_TASK_END_PROCESS";

    // Adjust/Edit
    public static final String INTEREST_EDIT_EXTERNAL_TASK_APPROVE = "INTEREST_EDIT_EXTERNAL_TASK_APPROVE";
    public static final String INTEREST_EDIT_EXTERNAL_TASK_EDIT = "INTEREST_EDIT_EXTERNAL_TASK_EDIT";
    public static final String INTEREST_EDIT_EXTERNAL_TASK_END_PROCESS = "INTEREST_EDIT_EXTERNAL_TASK_END_PROCESS";

    // ==================== Search/Query Fields ===========================================
    public static final String ORG_CODE = "org_code";
    public static final String INTEREST_RATE_TYPE ="interest_rate_type";
    public static final String APPLY_TYPE ="apply_type";

    // ==================== Mail Configuration =============================================
    public static final String MAIL_CODE_INTEREST_REGISTER = "LNM.100.01.01";
    public static final String MAIL_CODE_INTEREST_UPDATE = "LNM.101.01.01";
    // =================== TASK DEFINITION KEY ==========================================
    public static final String INTEREST_REGISTER_APPROVE_DEF = "LNM.100.01.01";
    public static final String INTEREST_REGISTER_EDIT_DEF = "LNM.100.01.02";
    public static final String INTEREST_ADJUST_APPROVE_DEF = "LNM.101.01.01";
    public static final String INTEREST_ADJUST_EDIT_DEF = "LNM.101.01.02";


    // ==================== Process Configs (Register) ===================================
    public static final String INTEREST_REGISTER_TXN_CONTENT_CODE = "LNM.100.01.01";

    // ==================== Process Configs (Adjust) ======================================
    public static final String INTEREST_EDIT_TXN_CONTENT_CODE = "LNM.101.01.01";

    // ==================== Templates & Files ====================================================
    public static final String INTEREST_REGISTER_PROCESS_TYPE_CODE = "LNM.100.01";
    public static final String INTEREST_EDIT_PROCESS_TYPE_CODE = "LNM.101.01";

    // =================== TYPE =============================================
    public static final String PROCESS_EDIT_TYPE = "EDIT_PROCESS";
    public static final String PROCESS_REGISTER_TYPE = "REGISTER_PROCESS";
    public static final String TIER = "CM091.002";
    public static final String FIXED = "CM091.001";
    // =================== EXCEL =============================================
    public static final String INTEREST_RATE_CODE = "Mã lãi suất";
    public static final String INTEREST_RATE_NAME = "Tên lãi suất";
    public static final String INTEREST_RATE_TYPE_NAME = "Loại lãi suất";
    public static final String CURRENCY_TYPE = "Loại tiền tệ";

    // =======================================================================================

    // =========== Product ===============
    public static final String PREFIX_PRODUCT_REGISTER = "LNM.102.01";
    public static final String PREFIX_PRODUCT_EDIT = "LNM.103.01";

    public static final String PRODUCT_REGISTER_PROCESS_TYPE_CODE = "LNM.100.01";
    public static final String PRODUCT_REGISTER_TXN_CONTENT_CODE = "LNM.102.01.01";

    public static final String PRODUCT_EXTERNAL_TASK_APPROVE = "PRODUCT_EXTERNAL_TASK_APPROVE";
    public static final String PRODUCT_EXTERNAL_TASK_EDIT = "PRODUCT_EXTERNAL_TASK_EDIT";
    public static final String PRODUCT_EXTERNAL_TASK_END_PROCESS = "PRODUCT_EXTERNAL_TASK_END_PROCESS";

    public static final String PRODUCT_EDIT_EXTERNAL_TASK_APPROVE = "PRODUCT_EDIT_EXTERNAL_TASK_APPROVE";
    public static final String PRODUCT_EDIT_EXTERNAL_TASK_EDIT = "PRODUCT_EDIT_EXTERNAL_TASK_EDIT";
    public static final String PRODUCT_EDIT_EXTERNAL_TASK_END_PROCESS = "PRODUCT_EDIT_EXTERNAL_TASK_END_PROCESS";

    public static final String PRODUCT_EDIT_TXN_CONTENT_CODE = "LNM.103.01.01";
    public static final String PRODUCT_EDIT_PROCESS_TYPE_CODE = "LNM.103.01";

    public static final String MAIL_CODE_PRODUCT_REGISTER = "LNM.102.01.01";
    public static final String MAIL_CODE_PRODUCT_UPDATE = "LNM.103.01.01";

    public static final String PRODUCT_REGISTER_APPROVE_DEF = "LNM.102.01.01";
    public static final String PRODUCT_REGISTER_EDIT_DEF = "LNM.102.01.02";
    public static final String PRODUCT_ADJUST_APPROVE_DEF = "LNM.103.01.01";
    public static final String PRODUCT_ADJUST_EDIT_DEF = "LNM.103.01.02";

    // =================== EXCEL =============================================
    public static final String PRODUCT_CODE = "Mã sản phẩm";
    public static final String PRODUCT_NAME = "Tên sản phẩm";
    public static final String PRODUCT_TYPE_NAME = "Loại sản phẩm";

    public static final String EFFECTIVE_DATE = "Từ ngày";
    public static final String INTEREST_CALC_METHOD = "Phương pháp tinnh lãi";
    public static final String INTEREST_BASE_CODE = "Cơ sở tính lãi";
    public static final String INTEREST_DATE_METHOD = "Phương pháp lấy ngày tính lãi";


    // =========== Common ===============
    public static final String APPROVAL = "APPROVAL";
    public static final String ACTIVE = "ACTIVE";
    public static final String MSG_ERR = "MSG_ERR";



    // ======================= TYPE =============================
    public static final String CREATE = "CREATE";
    public static final String UPDATE = "UPDATE";
    private LoanVariableConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
