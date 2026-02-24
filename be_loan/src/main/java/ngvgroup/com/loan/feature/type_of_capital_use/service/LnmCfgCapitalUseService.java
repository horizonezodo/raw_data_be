package ngvgroup.com.loan.feature.type_of_capital_use.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.loan.feature.type_of_capital_use.dto.LnmCfgCapitalUseDTO;
import ngvgroup.com.loan.feature.type_of_capital_use.model.LnmCfgCapitalUse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface LnmCfgCapitalUseService extends BaseService<LnmCfgCapitalUse, LnmCfgCapitalUseDTO> {
    Page<LnmCfgCapitalUseDTO> search(String keyword, Pageable pageable);
    ResponseEntity<byte[]> exportToExcel(String fileName);
    List<String> getAllCode();
}
