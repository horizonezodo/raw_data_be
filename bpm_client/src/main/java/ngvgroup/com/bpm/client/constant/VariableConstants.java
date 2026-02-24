package ngvgroup.com.bpm.client.constant;

public class VariableConstants {
    public static final String BUSINESS_DATA_VARIABLE = "businessData";
    public static final String TASK_BPM_DATA_VARIABLE = "taskBpmData";
    public static final String PROCESS_DATA_VARIABLE = "processData";
    public static final String APPROVAL_RESULT_VARIABLE = "approvalResult";
    public static final String TENANT_ID_VARIABLE = "tenantId";

    public static final String LOGIC_ERROR = "LOGIC_ERROR";

    // ==================== Camunda Task Actions ====================
    public static final String COMPLETE = "COMPLETE";
    public static final String CANCEL = "CANCEL";
    public static final String APPROVE = "APPROVE";
    public static final String REJECT = "REJECT";
    public static final String SEND_APPROVE = "SEND_APPROVE";

    private VariableConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
