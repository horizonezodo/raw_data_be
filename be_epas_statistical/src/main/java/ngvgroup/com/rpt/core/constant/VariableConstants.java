package ngvgroup.com.rpt.core.constant;

public class VariableConstants {

    public static final String DD = "approval";
    public static final String ORG = "%";
    public static final String CREATED = "CREATED";
    public static final String UPDATE = "UPDATED";
    public static final String COMPLETE = "COMPLETE";
    public static final String COMPLETED = "COMPLETED";
    public static final String ACTIVE = "ACTIVE";
    public static final String CANCEL = "CANCEL";
    public static final String REJECT = "REJECT";
    public static final String ACHIEVED = "ACHIEVED";
    public static final String BREACHED  = "BREACHED";
    public static final String UNASSIGNED = "UNASSIGNED";
    public static final String ACCEPTED = "ACCEPTED";
    public static final String CHECK = "CHECK";
    public static final String NEW = "NEW";

    public static final String CREATING_WORKFLOW_LOG = "Creating workflow with transitions: {}";
    public static final String CREATING_WORKFLOW_ID_LOG = "Workflow created with ID: {}";
    public static final String UPDATE_WORKFLOW_LOG = "Updating workflow with transitions: {}";
    public static final String UPDATE_WORKFLOW_ID_LOG = "Workflow updated with ID: {}";
    public static final String GETTING_WORKFLOW_LOG = "Getting workflow with transitions: {}";
    public static final String TRANSACTION_CREATE_LOG = "Transition created: {}";
    public static final String POST_FUNCTION_TRANSACTION_CREATE_LOG = "Post-function created for transition: {}";
    public static final String DELETE_ALL_TRANSACTION_WORKFLOW_LOG = "Deleted all transitions for workflow: {}";
    public static final String ERROR_GETTING_COMMON_LOG = "Error getting common name for code: {}, error: {}";
    public static final String ERROR_EXPORT_EXCEL_LOG = "Error: Lỗi export file Excel";
    public static final String AUTHENTICATION_FAILED = "Authentication failed";
    public static final String AUTHENTICATION_MODEL_FAILED = "Failed to get authenticate model: {}";

    public static final String GENERATE_DATA_NORMAL = "CM005.001";
    public static final String GENERATE_DATA_MODIFIED = "CM005.002";

    public static final String GUI_IN_TERM = "Trong hạn";
    public static final String GUI_OVERDUE = "Quá hạn";
    public static final String GUI_NEAR_TERM = "Gần đến hạn";

    public static final String POSTING_DATA_NORMAL = "N";     // CM005.001
    public static final String POSTING_DATA_MODIFIED = "M";   // CM005.002

    public static final String GUI_IN_TERM_CODE = "CM114.001";
    public static final String GUI_OVERDUE_CODE = "CM114.002";

    public static final String SEND_STATUS_SI = "SI";
    public static final String SEND_STATUS_BI = "BI";

    public static final String TEMPLATE_DATA_EDIT = "CM095.001";

    public static final String UNIT_CODE_MINUTE = "CM033.002";
    public static final String UNIT_CODE_HOUR   = "CM033.001";

    public static final String UNIT_TEXT_MINUTE  = "MINUTE";
    public static final String UNIT_TEXT_MINUTES = "MINUTES";
    public static final String UNIT_TEXT_HOUR    = "HOUR";
    public static final String UNIT_TEXT_HOURS   = "HOURS";
    public static final String UNIT_TEXT_DAY     = "DAY";
    public static final String UNIT_TEXT_DAYS    = "DAYS";

    public static final long MILLIS_PER_MINUTE = 60_000L;
    public static final long MILLIS_PER_HOUR   = 3_600_000L;
    public static final long MILLIS_PER_DAY    = 86_400_000L;

    public static final String TO_CHAR = "TO_CHAR";

    public static final Integer IS_VOID_NO = 0;
    public static final Integer IS_VOID_YES = 1;

    public static final Integer IS_FINAL_NONE = 0;
    public static final Integer IS_FINAL_SUCCESS = 1;
    public static final Integer IS_FINAL_VOID = 2;
    public static final Integer IS_FINAL_REJECT = 3;

    public static final String RPT_PERIOD_DAY = "CM094.001";
    public static final String RPT_PERIOD_MONTH = "CM094.002";
    public static final String RPT_PERIOD_QUARTER = "CM094.003";
    public static final String RPT_PERIOD_YEAR = "CM094.004";

    public static final String TABLEAU_DOMAIN = "TABLEAU_DOMAIN";

    public static final String COMMON_KPI_DETAIL_SP = "SP_STAT_COMMON_KPI_DETAIL";

    private VariableConstants() {
    }

}