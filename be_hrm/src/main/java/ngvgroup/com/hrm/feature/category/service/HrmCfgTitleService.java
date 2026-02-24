package ngvgroup.com.hrm.feature.category.service;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.hrm.feature.category.dto.HrmCfgTitleDTO;
import ngvgroup.com.hrm.feature.category.model.HrmCfgTitle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface HrmCfgTitleService extends BaseService<HrmCfgTitle, HrmCfgTitleDTO> {
    Page<HrmCfgTitleDTO> search(String keyword, Pageable pageable);
    HrmCfgTitleDTO getDetail(String titleCode);
    ResponseEntity<byte[]> exportExcel(String fileName) throws BusinessException;
    void deleteByTitleCode(String titleCode) throws BusinessException;
}
