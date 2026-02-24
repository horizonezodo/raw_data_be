package ngvgroup.com.hrm.feature.category.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.hrm.core.constant.HrmErrorCode;
import ngvgroup.com.hrm.feature.category.dto.ExportHrmCfgTitleDTO;
import ngvgroup.com.hrm.feature.category.dto.HrmCfgTitleDTO;
import ngvgroup.com.hrm.feature.category.mapper.HrmCfgTitleMapper;
import ngvgroup.com.hrm.feature.category.model.HrmCfgTitle;
import ngvgroup.com.hrm.feature.category.repository.HrmCfgTitleRepository;
import ngvgroup.com.hrm.feature.category.service.HrmCfgTitleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HrmCfgTitleServiceImpl extends BaseServiceImpl<HrmCfgTitle, HrmCfgTitleDTO> implements HrmCfgTitleService {
    private final HrmCfgTitleRepository hrmCfgTitleRepository;
    private final HrmCfgTitleMapper hrmCfgTitleMapper;
    private final ExportExcel exportExcel;

    public HrmCfgTitleServiceImpl(
            HrmCfgTitleRepository hrmCfgTitleRepository,
            HrmCfgTitleMapper hrmCfgTitleMapper, ExportExcel exportExcel) {
        super(hrmCfgTitleRepository, hrmCfgTitleMapper);
        this.hrmCfgTitleRepository = hrmCfgTitleRepository;
        this.hrmCfgTitleMapper = hrmCfgTitleMapper;
        this.exportExcel = exportExcel;
    }

    @Override
    public void validateBeforeCreate(HrmCfgTitleDTO dto) {
        this.hrmCfgTitleRepository.findByTitleCode(dto.getTitleCode())
            .ifPresent(existing -> {
                throw new BusinessException(HrmErrorCode.TITLE_CODE_EXIST, dto.getTitleCode());
            });
    }

    @Override
    protected void validateBeforeUpdate(HrmCfgTitleDTO dto, HrmCfgTitle e) {
        if (dto.getTitleCode().equals(e.getTitleCode())) {
            return;
        }
        validateBeforeCreate(dto);
    }

    @Override
    public Page<HrmCfgTitleDTO> search(String keyword, Pageable pageable) {
        return hrmCfgTitleRepository.search(keyword,pageable);
    }

    @Override
    public HrmCfgTitleDTO getDetail(String titleCode) {
        Optional<HrmCfgTitle> title = hrmCfgTitleRepository.findByTitleCode(titleCode);
        if(title.isPresent()){
            return hrmCfgTitleMapper.toDto(title.get());
        }
        else{
            throw new BusinessException(ErrorCode.NOT_FOUND, titleCode);
        }
    }

    @Override
    public ResponseEntity<byte[]> exportExcel(String fileName) throws BusinessException{

        List<ExportHrmCfgTitleDTO> lst = hrmCfgTitleRepository.exportData();
        try {
            return exportExcel.exportExcel(lst,fileName);
        } catch (Exception e) {
            throw new BusinessException(HrmErrorCode.EXPORT_EXCEL_ERROR);
        }
    }

    @Override
    public void deleteByTitleCode(String titleCode) throws BusinessException {
        if (isUsed(titleCode)) {
            throw new BusinessException(HrmErrorCode.TITLE_CODE_IS_USED, titleCode);
        }
        hrmCfgTitleRepository.deleteByTitleCode(titleCode);
    }

    private boolean isUsed(String titleCode) {
        return hrmCfgTitleRepository.checkUsed(titleCode) > 0;
    }
}
