package ngvgroup.com.hrm.feature.category.service;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.hrm.feature.category.dto.HrmCfgPositionDTO;
import ngvgroup.com.hrm.feature.category.model.HrmCfgPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface HrmCfgPositionService extends BaseService<HrmCfgPosition, HrmCfgPositionDTO> {
    Page<HrmCfgPositionDTO> search(String keyword, Pageable pageable);
    ResponseEntity<byte[]> exportExcel(String fileName) throws BusinessException;
}
