package ngvgroup.com.crm.core.constant;

public class CrmVariableConstants {

    // ==================== Common ====================
    public static final String CUSTOMER_TYPE_INDIVIDUAL = "CM007.001";

    // ==================== Camunda Process Keys ====================
    public static final String PROCESS_KEY_CUSTOMER_REGISTER = "CRM.200.01";
    public static final String PROCESS_KEY_CUSTOMER_ADJUST = "CRM.201.01";

    // ==================== External Task Topics ====================
    // Register
    public static final String REGISTER_EXTERNAL_TASK_APPROVE = "CRM_200_EXTERNAL_TASK_APPROVE";
    public static final String REGISTER_EXTERNAL_TASK_EDIT = "CRM_200_EXTERNAL_TASK_EDIT";
    public static final String REGISTER_EXTERNAL_TASK_END_PROCESS = "CRM_200_EXTERNAL_TASK_END_PROCESS";

    // Adjust/Edit
    public static final String ADJUST_EXTERNAL_TASK_APPROVE = "CRM_201_EXTERNAL_TASK_APPROVE";
    public static final String ADJUST_EXTERNAL_TASK_EDIT = "CRM_201_EXTERNAL_TASK_EDIT";
    public static final String ADJUST_EXTERNAL_TASK_END_PROCESS = "CRM_201_EXTERNAL_TASK_END_PROCESS";

    // ==================== Search/Query Fields ====================
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String IDENTIFICATION_ID = "identification_id";

    // ==================== Mail Configuration ====================
    public static final String MAIL_TEMPLATE_CODE_REGISTER = "CRM.200.01.001";
    public static final String MAIL_TEMPLATE_CODE_ADJUST = "CRM.201.01.001";

    // ==================== Mail Parameter Keys ====================
    public static final String MAIL_PARAM_PROCESS_INSTANCE_CODE = "PROCESS_INTANCE_CODE";
    public static final String MAIL_PARAM_KS_QHKH = "KS_QHKH";
    public static final String MAIL_PARAM_COMMENT = "COMMENT";

    // ==================== Process Configs (Register) ====================
    public static final String TXN_CONTENT_CODE_REGISTER = "CRM.200.01.01";

    // ==================== Process Configs (Adjust) ====================
    public static final String TXN_CONTENT_CODE_ADJUST = "CRM.201.01.01";

    // ==================== Templates & Files ====================
    public static final String CUSTOMER_REGISTER_TEMPLATE_FILE_CODE = "AT.CRM.0001";
    public static final String CUSTOMER_ADJUST_TEMPLATE_FILE_CODE = "AT.CRM.0002";

    // ==================== Work Time Units ====================
    public static final String WORK_TIME_UNIT_WORK_YEARS = "WORK_YEARS";
    public static final String WORK_TIME_UNIT_YEARS = "YEARS";
    public static final String WORK_TIME_UNIT_WORK_MONTHS = "WORK_MONTHS";
    public static final String WORK_TIME_UNIT_MONTHS = "MONTHS";

    private CrmVariableConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
