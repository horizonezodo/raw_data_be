package ngvgroup.com.fac.core.constant;

import java.util.Map;

public class FacVariableConstants {

    private FacVariableConstants(){}

    public static final String ORG_CODE_ALL = "%";
    public static final String PREFIX_ACCOUNT_ENTRY_INIT = "FAC.200.01";
    public static final String PREFIX_SHEET = "FAC.201.01";
    public static final String FAC_TXN_ACCT_TABLE = "FAC_TXN_ACCT";
    public static final String FAC_TXN_ACCT_ENTRY_TABLE = "FAC_TXN_ACCT_ENTRY";
    public static final String PROCESS_INSTANCE_CODE = "PROCESS_INSTANCE_CODE";
    public static final String TXN_ACCT_ENTRY_CODE = "TXN_ACCT_ENTRY_CODE";
    public static final String ACTIVE = "ACTIVE";
    public static final String CURRENCY_VND = "VND";
    public static final String FAC_PROCESS_TYPE_CODE = "FAC.200.01";
    public static final Map<String, String> COLUMN_TO_PROPERTY = Map.of(
            "ORG_CODE", "orgCode",
            "PREFIX", "prefix",
            "LENGTH_SEQ", "lengthSeq",
            "VOUCHER_TYPE_CODE", "voucherTypeCode",
            "VOUCHER_TYPE_NAME", "voucherTypeName",
            "PERIOD_TYPE", "periodType",
            "SEPARATOR", "separator",
            "START_SEQ", "startSeq"
    );
    public static final String FAC_CFG_VOUCHER_SEQ = "FAC_CFG_VOUCHER_SEQ";
    public static final String CURRENT_SEQ = "CURRENT_SEQ";
    public static final String COMPLETE = "COMPLETE";
    public static final String CANCEL = "CANCEL";
    public static final String APPROVE = "APPROVE";
    public static final String REJECT = "REJECT";
    public static final String SEND_APPROVE = "SEND_APPROVE";
    public static final String BUSINESS_DATA_VARIABLE = "businessData";
    public static final String MAIL_TEMPLATE_CODE = "FAC.200.01.001";
    public static final String FAC_TXN_ACCT_ENTRY_DTL = "FAC_TXN_ACCT_ENTRY_DTL";
    public static final String TXN_ACCT_ENTRY_DTL_CODE = "TXN_ACCT_ENTRY_DTL_CODE";
    public static final String FAC_CFG_ACCT_ENTRY = "FAC_CFG_ACCT_ENTRY";
    public static final String PREFIX_ENT = "ENT";
    public static final String ENTRY_CODE = "ENTRY_CODE";
    public static final String DEBIT="D";
    public static final String CREDIT="C";
    public static final String BOTH="B";
    public static final String ENTRY_ACCT_EXTERNAL_TASK_APPROVE = "FAC_200_TASK_APPROVE";
    public static final String ENTRY_ACCT_EXTERNAL_TASK_EDIT = "FAC_200_TASK_EDIT";
    public static final String ENTRY_ACCT_EXTERNAL_TASK_END_PROCESS = "FAC_200_TASK_END_PROCESS";
    public static final String EXTERNAL_TASK_APPROVE_DOUBLE_ENTRY_ACCT="EXTERNAL_TASK_APPROVE_DOUBLE_ENTRY_ACCT";
    public static final String EXTERNAL_TASK_EDIT_DOUBLE_ENTRY_ACCT="EXTERNAL_TASK_EDIT_DOUBLE_ENTRY_ACCT";
    public static final String EXTERNAL_TASK_END_PROCESS_DOUBLE_ENTRY_PROCESS="EXTERNAL_TASK_END_PROCESS_DOUBLE_ENTRY_PROCESS";
    public static final String FAC_201_EXTERNAL_TASK_APPROVE = "FAC_201_EXTERNAL_TASK_APPROVE";
    public static final String FAC_201_EXTERNAL_TASK_EDIT = "FAC_201_EXTERNAL_TASK_EDIT";
    public static final String FAC_201_EXTERNAL_TASK_END_PROCESS = "FAC_201_EXTERNAL_TASK_END_PROCESS";
    public static final String SINGLE_ENTRY_ACCT_TEMPLATE_FILE_CODE = "AT.FAC.0001";
    public static final String SINGLE_ENTRY_ACCT_CODE_INIT = "FAC.200.01.01";
    public static final String SINGLE_ENTRY_ACCT_CODE_EDIT = "FAC.200.01.02";
    public static final String CUSTOMER_CODE="customer_code";
    public static final String CUSTOMER_NAME="customer_name";
    public static final String MAIL_PARAM_PROCESS_INSTANCE_CODE = "PROCESS_INTANCE_CODE";
    public static final String MAIL_PARAM_KS_QHKH = "KS_QHKH";
    public static final String DOUBLE_ACCT_PROCESS_FILE = "AT.CRM.0002";
    public static final String SHEET_KEY_PROCESS = "FAC.201.01";
    public static final String DOUBLE_ACCT_PROCESS_TYPE_CODE = "FAC.202.01";
    public static final String PREFIX_DOUBLE_ACCT= "FAC.202.01";
    public static final String MAIL_PROCESS_INSTANCE_CODE = "PROCESS_INSTANCE_CODE";
    public static final String MAIL_TEMPLATE_REJECT_SHEET = "FAC.201.01.001";
    public static final String MAIL_PARAM_APPROVER = "User KS.GDV";
    public static final String MAIL_PARAM_COMMENT = "Nội dung Comments";
    public static final String MAIL_TEMPLATE_CODE_DOUBLE_ENTRY = "FAC.202.01.001";
    public static final String PREFIX_PROC = "PROC";
    public static final String FAC_CFG_ACCT_PROCESS = "FAC_CFG_ACCT_PROCESS";
    public static final String ACCT_PROCESS_CODE = "ACCT_PROCESS_CODE";
    public static final String DOUBLE_ENTRY_ACCT_APPROVE = "FAC.202.01.01";
    public static final String DOUBLE_ENTRY_ACCT_EDIT = "FAC.202.01.02";

}
