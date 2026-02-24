package ngvgroup.com.fac.feature.regulated_account.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.fac.feature.regulated_account.dto.CoaAccFilter;
import ngvgroup.com.fac.feature.regulated_account.dto.CtgCfgCoaAccDTO;
import ngvgroup.com.fac.feature.regulated_account.dto.CtgCfgCoaAccResDTO;
import ngvgroup.com.fac.feature.regulated_account.model.CtgCfgCoaAcc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgCoaAccService extends BaseService<CtgCfgCoaAcc, CtgCfgCoaAccDTO> {
    Page<CtgCfgCoaAccResDTO> pageCoaAcc(CoaAccFilter filter, Pageable pageable);
    ResponseEntity<byte[]> exportToExcel( String fileName,List<String> isInternals);
    CtgCfgCoaAccDTO getByAccCoaCode(String accCoaCode);
    List<CtgCfgCoaAccDTO> getAllByIsInternal(String isInternal,String accCoaCode);
    CtgCfgCoaAccDTO getByAccCoaCodeAndIsInternal(String accCoaCode,String isInternal);
}
