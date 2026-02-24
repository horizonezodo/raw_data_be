package ngvgroup.com.bpm.core.base.service;

import java.util.List;

import ngvgroup.com.bpm.core.base.dto.*;
import ngvgroup.com.bpm.features.transaction.dto.CustomerTransactionHistoryDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;

public interface BpmService {

        TaskViewBpmData getDetail(String taskId);

        void claimTask(String taskId);

        List<ProcessFileDto> getProcessFilesFromReferenceCode(String referenceCode, String processTypeCode);

        void saveDraft(DraftTaskBpmData request);

        FileDto getFileDetail(String fileId);

        // SLA
        void applySlaSettings(ngvgroup.com.bpm.core.base.model.BpmTxnProcessInstance pi, String orgCode);

        void applySla(ngvgroup.com.bpm.core.base.model.BpmTxnTaskInbox inbox, String orgCode, String taskDefKey,
                        String procTypeCode);

        // Audit
        void buildAudits(ngvgroup.com.bpm.core.base.dto.ProcessData processData,
                        ngvgroup.com.bpm.core.base.dto.TaskBpmData bpmData, java.sql.Timestamp now);

        void persistAuditsForComplete(String orgCode, String piCode, String createdBy, java.sql.Timestamp now,
                        ngvgroup.com.bpm.core.base.dto.TaskBpmData taskBpmData);

        // DocFile
        void persistFilesForStart(ngvgroup.com.bpm.core.base.model.BpmTxnProcessInstance processInstance,
                        ngvgroup.com.bpm.core.base.dto.TaskBpmData taskBpmData,
                        ngvgroup.com.bpm.core.base.dto.ProcessData processData,
                        java.sql.Timestamp now);

        void handleFilesForResubmit(String taskId, String processInstanceCode,
                        ngvgroup.com.bpm.core.base.dto.TaskBpmData data,
                        ngvgroup.com.bpm.core.base.dto.ProcessData processData,
                        String currentUser, java.sql.Timestamp now);

        void finalizeDocFiles(String piCode, String currentUser, String endStatus);

        String getTaskDefinitionKeyFromCamunda(String taskId);

        ProcessInstanceDto startProcess(String processDefinitionKey,
                                        StartProcessRequestDto requestBody);

        List<CustomerTransactionHistoryDto> getCustomerTransactionHistory(String customerCode, List<String> processTypeCode);

        void rollback(StartResponse response);
}
