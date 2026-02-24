package ngvgroup.com.crm.features.customer.service;

import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.dto.shared.AttachmentContext;
// cleaned up unused imports
import ngvgroup.com.crm.features.common.dto.TemplateResDto;
import ngvgroup.com.crm.features.customer.dto.profile.CustomerProfileDTO;

public interface CustomerBpmService {

        /**
         * Build attachment context for file attachment in BPM process.
         * Only builds the context data, file generation is delegated to BPM side.
         * 
         * @param processFileCode Mã code của file trong quy trình (VD: F001)
         * @param businessData    Dữ liệu khách hàng để điền vào template
         * @return AttachmentContext chứa processFileCode và context data
         */
        AttachmentContext buildAttachmentContext(String processFileCode, CustomerProfileDTO businessData);

        /**
         * Sinh file biểu mẫu (DOCX) từ dữ liệu khách hàng.
         *
         * @param businessData Dữ liệu khách hàng
         * @return Nội dung file dưới dạng byte array
         */
        byte[] generateBusinessFile(CustomerProfileDTO businessData, TemplateResDto template);

        /**
         * Build Mail Variable for User Task
         */
        MailVariableDto buildMailVariable(String templateCode, SubmitTaskRequest<CustomerProfileDTO> dto);

        /**
         * Build Process Data for Start Process
         */
        ProcessData buildProcessData(StartRequest<CustomerProfileDTO> dto, String txnContentCode);
}
