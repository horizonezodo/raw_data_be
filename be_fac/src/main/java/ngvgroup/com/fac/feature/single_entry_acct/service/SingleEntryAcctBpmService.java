package ngvgroup.com.fac.feature.single_entry_acct.service;

import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.shared.AttachmentContext;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.fac.feature.common.dto.TemplateResDto;
import ngvgroup.com.fac.feature.single_entry_acct.dto.SingleEntryAcctDTO;

public interface SingleEntryAcctBpmService {

        /**
         * Tạo file biểu mẫu (DOCX) từ dữ liệu khách hàng và đính kèm vào map file để
         * gửi sang BPM.
         * 
         * @param processFileCode Mã code của file trong quy trình (VD: F001)
         * @param businessData    Dữ liệu khách hàng để điền vào template
         * @return AttachmentContext chứa processFileCode và context data
         */
        AttachmentContext buildAttachmentContext(String processFileCode, SingleEntryAcctDTO businessData);

        /**
         * Sinh file biểu mẫu (DOCX) từ dữ liệu khách hàng.
         *
         * @param businessData    Dữ liệu khách hàng
         * @return Nội dung file dưới dạng byte array
         */
        byte[] generateBusinessFile(SingleEntryAcctDTO businessData, TemplateResDto template);

        /**
         * Build Mail Variable for User Task
         */
        MailVariableDto buildMailVariable(SubmitTaskRequest<SingleEntryAcctDTO> dto);

        /**
         * Build Process Data for Start Process
         */
        ProcessData buildProcessData(StartRequest<SingleEntryAcctDTO> dto, String txnContentCode);
}
