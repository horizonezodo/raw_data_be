package ngvgroup.com.crm.features.crm_cfg_project_type.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.crm.features.crm_cfg_project_type.dto.CrmCfgProjectTypeDto;
import ngvgroup.com.crm.features.crm_cfg_project_type.model.CrmCfgProjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CrmCfgProjectTypeService extends BaseService<CrmCfgProjectType, CrmCfgProjectTypeDto> {
    Page<CrmCfgProjectTypeDto> search(String keyword, Pageable pageable);
    ResponseEntity<byte[]> exportToExcel(String keyword, String fileName);
    boolean existsByProjectTypeCode(String projectTypeCode);
}