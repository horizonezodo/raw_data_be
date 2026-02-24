package ngvgroup.com.fac.feature.double_entry_accounting.service;

import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.shared.AttachmentContext;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.fac.feature.common.dto.TemplateResDto;
import ngvgroup.com.fac.feature.double_entry_accounting.dto.DoubleEntryAccountingProcessDto;
public interface DoubleEntryBpmService {
    /**
     * Build attachment context for file attachment in BPM process.
     * Only builds the context data, file generation is delegated to BPM side.
     *
     * @param processFileCode Mã code của file trong quy trình (VD: F001)
     * @param businessData    Dữ liệu khách hàng để điền vào template
     * @return AttachmentContext chứa processFileCode và context data
     */
    AttachmentContext buildAttachmentContext(String processFileCode, DoubleEntryAccountingProcessDto businessData);

    /**
     * Sinh file biểu mẫu (DOCX) từ dữ liệu khách hàng.
     *
     * @param businessData Dữ liệu khách hàng
     * @return Nội dung file dưới dạng byte array
     */
    byte[] generateBusinessFile(DoubleEntryAccountingProcessDto businessData, TemplateResDto template);

    /**
     * Build Mail Variable for User Task
     */
    MailVariableDto buildMailVariable(String templateCode,SubmitTaskRequest<DoubleEntryAccountingProcessDto> dto);

    /**
     * Build Process Data for Start Process
     */
    ProcessData buildProcessData(StartRequest<DoubleEntryAccountingProcessDto> dto, String txnContentCode,String processInstanceCode);
}
