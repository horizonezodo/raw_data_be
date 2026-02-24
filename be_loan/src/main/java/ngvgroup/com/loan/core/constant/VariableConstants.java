package ngvgroup.com.loan.core.constant;

public class VariableConstants {

    // ==================== Helpers ====================
    public static final String SLA_START_TIME_MODE = "SLA_START_TIME_MODE";
    public static final String UNIT = "CM033.001";

    // ==================== Status ====================
    public static final String COMPLETE = "COMPLETE";
    public static final String CANCEL = "CANCEL";
    public static final String APPROVE = "APPROVE";
    public static final String REJECT = "REJECT";
    public static final String SEND_APPROVE = "SEND_APPROVE";
    public static final String APPROVAL = "APPROVAL";
    public static final String ACTIVE = "ACTIVE";

    public static final String CUSTOMER_TYPE_INDIVIDUAL = "CM007.001";


    // ==================== Process variables ====================
    public static final String APPROVAL_RESULT_VARIABLE = "approvalResult";
    public static final String AUDIT_DATA = "auditData";
    public static final String PROCESS_DATA_VARIABLE = "processData";
    public static final String FILE_DATA_VARIABLE = "fileData";
    public static final String COMPLETE_DATA_VARIABLE = "completeData";
    public static final String MAIL_DATA_VARIABLE = "mailData";
    public static final String UPDATE_DATA_VARIABLE = "updateData";

    // ==================== Interest Process ====================
    public static final String PROCESS_KEY_INTEREST_REGISTER = "interest_registration";
    public static final String PROCESS_KEY_INTEREST_UPDATE = "interest_update";

    public static final String PREFIX_INTEREST_REGISTER = "LNM.100.01";
    public static final String PREFIX_INTEREST_EDIT = "LNM.101.01";
    public static final String MAIL_CODE_INTEREST_REGISTER = "LNM.100.01.03";
    public static final String MAIL_CODE_INTEREST_UPDATE = "LNM.101.01.03";

    public static final String TIER = "CM091.002";
    public static final String FIXED = "CM091.001";
    public static final String INTEREST_REGISTER_PROCESS_TYPE_CODE = "LNM.100.01";

    public static final String INTEREST_REGISTER_TXN_CONTENT_CODE = "LNM.100.01.01";
    public static final String INTEREST_EDIT_TXN_CONTENT_CODE = "LNM.101.01.01";
    public static final String INTEREST_REGISTER_START_COMMENT = "Hoàn thành khởi tạo đăng ký lãi suất";
    public static final String INTEREST_RATE_CODE = "INTEREST_RATE_CODE";

    public static final String INTEREST_EDIT_PROCESS_TYPE_CODE = "LNM.101.01";
    public static final String INTEREST_EDIT_START_COMMENT = "Hoàn thành khởi tạo chỉnh sửa lãi suất";

    // ==================== Product Process ====================
    public static final String PROCESS_KEY_PRODUCT_REGISTER = "product_registration";
    public static final String PROCESS_KEY_PRODUCT_UPDATE = "product_update";

    public static final String PREFIX_PRODUCT_REGISTER = "LNM.102.01";
    public static final String MAIL_CODE_PRODUCT_REGISTER = "LNM.102.01.001";
    public static final String TASK_MAIL_CODE_PRODUCT_REGISTER = "LNM.102.01.03";
    public static final String TASK_MAIL_CODE_PRODUCT_UPDATE = "LNM.103.01.03";
    public static final String PRODUCT_REGISTER_PROCESS_TYPE_CODE = "LNM.102.01";
    public static final String PRODUCT_REGISTER_TXN_CONTENT_CODE = "LNM.102.01.01";
    public static final String PRODUCT_REGISTER_START_COMMENT = "Hoàn thành khởi tạo đăng ký sản phẩm";
    public static final String LNM_PRODUCT_CODE = "LNM_PRODUCT_CODE";

    public static final String PRODUCT_EDIT_PROCESS_TYPE_CODE = "LNM.103.01";
    public static final String PRODUCT_EDIT_TXN_CONTENT_CODE = "LNM.103.01.01";
    public static final String PRODUCT_EDIT_START_COMMENT = "Hoàn thành khởi tạo chỉnh sửa sản phẩm";

    // ==================== External Tasks ====================
    public static final String LOAN_EXTERNAL_TASK_APPROVE = "LOAN_EXTERNAL_TASK_APPROVE";
    public static final String LOAN_EXTERNAL_TASK_EDIT = "LOAN_EXTERNAL_TASK_EDIT";
    public static final String LOAN_EXTERNAL_TASK_END_PROCESS = "LOAN_EXTERNAL_TASK_END_PROCESS";

    public static final String INTEREST_EDIT_EXTERNAL_TASK_APPROVE = "INTEREST_EDIT_EXTERNAL_TASK_APPROVE";
    public static final String INTEREST_EDIT_EXTERNAL_TASK_EDIT = "INTEREST_EDIT_EXTERNAL_TASK_EDIT";
    public static final String INTEREST_EDIT_EXTERNAL_TASK_END_PROCESS = "INTEREST_EDIT_EXTERNAL_TASK_END_PROCESS";

    public static final String PRODUCT_EXTERNAL_TASK_APPROVE = "PRODUCT_EXTERNAL_TASK_APPROVE";
    public static final String PRODUCT_EXTERNAL_TASK_EDIT = "PRODUCT_EXTERNAL_TASK_EDIT";
    public static final String PRODUCT_EXTERNAL_TASK_END_PROCESS = "PRODUCT_EXTERNAL_TASK_END_PROCESS";

    public static final String PRODUCT_EDIT_EXTERNAL_TASK_APPROVE = "PRODUCT_EDIT_EXTERNAL_TASK_APPROVE";
    public static final String PRODUCT_EDIT_EXTERNAL_TASK_EDIT = "PRODUCT_EDIT_EXTERNAL_TASK_EDIT";
    public static final String PRODUCT_EDIT_EXTERNAL_TASK_END_PROCESS = "PRODUCT_EDIT_EXTERNAL_TASK_END_PROCESS";

    public static final String ERROR_TASK = "ERROR_TASK";

    public static final String INTEREST_REGISTER_PATH = "DKLS";
    public static final String PRODUCT_REGISTER_PATH = "DKSP";

    public static final String MSG_ERR = "MSG_ERR";
    public static final String DD = "DD";
    public static final String TENANT_ID_VARIABLE = "tenantId";

    public static final String ORG_CODE = "org_code";
    public static final String INTEREST_RATE_TYPE ="interest_rate_type";
    public static final String APPLY_TYPE ="apply_type";

    public static final String REGISTER_EXTERNAL_TASK_APPROVE = "LNM_100_EXTERNAL_TASK_APPROVE";
    public static final String REGISTER_EXTERNAL_TASK_EDIT = "LNM_100_EXTERNAL_TASK_EDIT";
    public static final String REGISTER_EXTERNAL_TASK_END_PROCESS = "LNM_100_EXTERNAL_TASK_END_PROCESS";

    private VariableConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

}
