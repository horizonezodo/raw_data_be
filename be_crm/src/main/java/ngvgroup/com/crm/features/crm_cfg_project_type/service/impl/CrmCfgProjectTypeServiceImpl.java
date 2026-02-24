package ngvgroup.com.crm.features.crm_cfg_project_type.service.impl;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.crm.core.constant.CrmErrorCode;
import ngvgroup.com.crm.features.crm_cfg_project_type.dto.CrmCfgProjectTypeDto;
import ngvgroup.com.crm.features.crm_cfg_project_type.mapper.CrmCfgProjectTypeMapper;
import ngvgroup.com.crm.features.crm_cfg_project_type.model.CrmCfgProjectType;
import ngvgroup.com.crm.features.crm_cfg_project_type.repository.CrmCfgProjectTypeRepository;
import ngvgroup.com.crm.features.crm_cfg_project_type.service.CrmCfgProjectTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CrmCfgProjectTypeServiceImpl
        extends BaseServiceImpl<CrmCfgProjectType, CrmCfgProjectTypeDto>
        implements CrmCfgProjectTypeService {

    private final CrmCfgProjectTypeRepository crmCfgProjectTypeRepository;
    private final ExportExcel exportExcel;

    public CrmCfgProjectTypeServiceImpl(CrmCfgProjectTypeRepository repository,
                                        CrmCfgProjectTypeMapper mapper,
                                        ExportExcel exportExcel) {
        super(repository, mapper);
        this.crmCfgProjectTypeRepository = repository;
        this.exportExcel = exportExcel;
    }

    @Override
    public  Page<CrmCfgProjectTypeDto> search(String keyword, Pageable pageable) {
        return crmCfgProjectTypeRepository.search(keyword, pageable);
    }

    @Override
    public ResponseEntity<byte[]> exportToExcel(String keyword, String fileName) {
        try {
            List<CrmCfgProjectTypeDto> dataList = crmCfgProjectTypeRepository.exportToExcel(keyword);

            return exportExcel.exportExcel(dataList, fileName);

        } catch (Exception e) {
            throw new BusinessException(CrmErrorCode.WRITE_EXCEL_ERROR, e);
        }
    }

    @Override
    public boolean existsByProjectTypeCode(String projectTypeCode){
        return crmCfgProjectTypeRepository.existsByProjectTypeCode(projectTypeCode);
    }
}