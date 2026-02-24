package ngvgroup.com.hrm.feature.category.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.hrm.core.constant.HrmErrorCode;
import ngvgroup.com.hrm.feature.category.dto.ExportHrmCfgPositionDTO;
import ngvgroup.com.hrm.feature.category.dto.HrmCfgPositionDTO;
import ngvgroup.com.hrm.feature.category.mapper.HrmCfgPositionMapper;
import ngvgroup.com.hrm.feature.category.model.HrmCfgPosition;
import ngvgroup.com.hrm.feature.category.repository.HrmCfgPositionRepository;
import ngvgroup.com.hrm.feature.category.service.HrmCfgPositionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HrmCfgPositionServiceImpl extends BaseServiceImpl<HrmCfgPosition, HrmCfgPositionDTO> implements HrmCfgPositionService {
    private final HrmCfgPositionRepository hrmCfgPositionRepository;
    private final ExportExcel exportExcel;

    public HrmCfgPositionServiceImpl(
            HrmCfgPositionRepository hrmCfgPositionRepository,
            HrmCfgPositionMapper hrmCfgPositionMapper,
            ExportExcel exportExcel) {
        super(hrmCfgPositionRepository, hrmCfgPositionMapper);
        this.hrmCfgPositionRepository = hrmCfgPositionRepository;
        this.exportExcel = exportExcel;
    }

    @Override
    public void validateBeforeCreate(HrmCfgPositionDTO dto) {
        this.hrmCfgPositionRepository.findByPositionCode(dto.getPositionCode())
                .ifPresent(existing -> {
                    throw new BusinessException(HrmErrorCode.POSITION_CODE_EXIST, dto.getPositionCode());
                });
    }

    @Override
    protected void validateBeforeUpdate(HrmCfgPositionDTO dto, HrmCfgPosition e) {
        if (dto.getPositionCode().equals(e.getPositionCode())) {
            return;
        }
        validateBeforeCreate(dto);
    }

    @Override
    public Page<HrmCfgPositionDTO> search(String keyword, Pageable pageable) {
        return hrmCfgPositionRepository.search(keyword,pageable);
    }

    @Override
    public ResponseEntity<byte[]> exportExcel(String fileName) throws BusinessException{
        List<ExportHrmCfgPositionDTO> lst = hrmCfgPositionRepository.exportData();
        try {
            return exportExcel.exportExcel(lst, fileName);
        } catch (Exception e) {
            throw new BusinessException(HrmErrorCode.EXPORT_EXCEL_ERROR);
        }
    }
}
