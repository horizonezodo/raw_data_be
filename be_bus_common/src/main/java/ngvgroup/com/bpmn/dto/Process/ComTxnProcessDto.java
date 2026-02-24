package ngvgroup.com.bpmn.dto.Process;

import java.util.Date;


public interface ComTxnProcessDto {
    String getCustomerCode();
    String getCustomerName();
    String getProcessInstanceCode();
    String getOrgName();
    String getTaskName();
    String getProcessTypeName();
    String getCreatedBy();
    Double getSlaMaxDuration();
    Date getCreatedDate();
    Date getSlaProcessDeadline();
    Date getModifiedDate();
    String getDescription();
    String getBusinessStatus();
    String getSlaResult();
    Double getSlaWarningDuration();
    String getSlaWarningType();
    Double getSlaWarningPercent();
    String getProcInstId();
    String getTaskId();
}
