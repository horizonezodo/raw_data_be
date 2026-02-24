package ngvgroup.com.loan.feature.product_proccess.service.impl;

import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.variable.InterpretiveStructureDto;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.product_proccess.dto.ProductProfileDTO;
import ngvgroup.com.loan.feature.product_proccess.service.ProductBpmService;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductBpmServiceImpl extends BaseStoredProcedureService implements ProductBpmService {
    @Override
    public String attachBusinessFile(String processFileCode, ProductProfileDTO businessData, MultiValueMap<String, MultipartFile> multiFileMap) {
        return "";
    }

    @Override
    public MailVariableDto buildMailVariable(SubmitTaskRequest<ProductProfileDTO> dto) {
        return null;
    }

    @Override
    public ProcessData buildProcessData(StartRequest<ProductProfileDTO> dto, String txnContentCode) {
        ProcessData processData = new ProcessData();
        processData.setReferenceCode(dto.getBusinessData().getLnmProductCode());
        processData.setOrgCode(dto.getBusinessData().getOrgCode());
        processData.setProcessInstanceCode(dto.getBusinessData().getProcessInstanceCode());
        processData.setTxnContent(txnContentCode);

        InterpretiveStructureDto interpretiveStructureDto = new InterpretiveStructureDto();
        interpretiveStructureDto.setContentCode(txnContentCode);
        interpretiveStructureDto.setParamInterpretiveStructure(buildInterpretiveStructure(dto.getBusinessData()));
        processData.setInterpretiveStructureDto(interpretiveStructureDto);
        return processData;
    }

    private Map<String, String> buildInterpretiveStructure(ProductProfileDTO businessData) {
        Map<String, String> interpretive = new HashMap<>();


        interpretive.put(LoanVariableConstants.ORG_CODE, businessData.getOrgCode());
        return interpretive;
    }
}
