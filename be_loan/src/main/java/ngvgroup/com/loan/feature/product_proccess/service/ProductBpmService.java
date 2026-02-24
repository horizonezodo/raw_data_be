package ngvgroup.com.loan.feature.product_proccess.service;

import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.loan.feature.product_proccess.dto.ProductProfileDTO;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

public interface ProductBpmService {
    String attachBusinessFile(String processFileCode, ProductProfileDTO businessData,
                              MultiValueMap<String, MultipartFile> multiFileMap);

    MailVariableDto buildMailVariable(SubmitTaskRequest<ProductProfileDTO> dto);

    ProcessData buildProcessData(StartRequest<ProductProfileDTO> dto, String txnContentCode);
}
