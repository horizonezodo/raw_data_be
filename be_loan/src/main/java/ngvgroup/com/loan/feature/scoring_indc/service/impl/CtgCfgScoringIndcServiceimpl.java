package ngvgroup.com.loan.feature.scoring_indc.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.feature.scoring_indc.dto.CtgCfgScoringIndcAdd;
import ngvgroup.com.loan.feature.scoring_indc.dto.CtgCfgScoringIndcDto;
import ngvgroup.com.loan.feature.scoring_indc.model.CtgCfgScoringIndc;
import ngvgroup.com.loan.feature.scoring_indc.repository.CtgCfgScoringIndcRepository;
import ngvgroup.com.loan.feature.scoring_indc.service.CtgCfgScoringIndcService;
import ngvgroup.com.loan.feature.scoring_indc_result.dto.CtgCfgScoringIndcResultDTO;
import ngvgroup.com.loan.feature.scoring_indc_result.service.CtgCfgScoringIndcResultService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CtgCfgScoringIndcServiceimpl extends BaseStoredProcedureService implements CtgCfgScoringIndcService {
    private final CtgCfgScoringIndcRepository ctgCfgScoringIndcRepository;
    private final CtgCfgScoringIndcResultService ctgCfgScoringIndcResultService;
    private final ExportExcel exportExcel;

    @Override
    public Page<CtgCfgScoringIndcDto> getAllScoringIndc(String keyword, Pageable pageable) {
        BigDecimal keywordNumber;

        try {
            keywordNumber = new BigDecimal(keyword);
        } catch (NumberFormatException ignored) {
            keywordNumber = null;
        }
        return ctgCfgScoringIndcRepository.findByKeyword(keyword,keywordNumber,pageable);
    }

    @Override
    public ResponseEntity<byte[]> exportToExcel(CtgCfgScoringIndcDto dto, String keyword, String fileName) {
        try {
            List<CtgCfgScoringIndcDto> lst = ctgCfgScoringIndcRepository.exportExcelData(keyword);
            return exportExcel.exportExcel(lst, fileName);
        }
        catch (Exception e) {
            throw new BusinessException(LoanErrorCode.WRITE_EXCEL_ERROR, e);
        }
    }

    @Override
    public void addScoringIndc(CtgCfgScoringIndcAdd dto) {
        if(ctgCfgScoringIndcRepository.existsByIndicatorCode(dto.getCtgCfgScoringIndcDto().getIndicatorCode())){
            throw new BusinessException(ErrorCode.CONFLICT, "Mã chỉ tiêu đã tồn tại");
        }
        CtgCfgScoringIndc ctgCfgScoringIndc = new CtgCfgScoringIndc();
        BeanUtils.copyProperties(dto.getCtgCfgScoringIndcDto(), ctgCfgScoringIndc);
        ctgCfgScoringIndc.setDescription(dto.getCtgCfgScoringIndcDto().getDesc());
        ctgCfgScoringIndc.setIsActive(1);
        ctgCfgScoringIndc.setIsDelete(0);
        ctgCfgScoringIndc.setRecordStatus("DD");
        ctgCfgScoringIndc.setCreatedBy(getCurrentUserName());
        ctgCfgScoringIndc.setApprovedBy(getCurrentUserName());
        ctgCfgScoringIndcRepository.save(ctgCfgScoringIndc);
        ctgCfgScoringIndcResultService.save(dto.getCtgCfgScoringIndcResultDTOS(),dto.getCtgCfgScoringIndcDto().getIndicatorCode());
    }

    @Override
    public void updateSrocingIndc(String indicatorCode, CtgCfgScoringIndcAdd add) {
        CtgCfgScoringIndc ctgCfgScoringIndc = ctgCfgScoringIndcRepository.findByIndicatorCode(indicatorCode);
        BeanUtils.copyProperties(add.getCtgCfgScoringIndcDto(),ctgCfgScoringIndc);
        ctgCfgScoringIndc.setIndicatorCode(indicatorCode);
        ctgCfgScoringIndcRepository.save(ctgCfgScoringIndc);
        ctgCfgScoringIndcResultService.update(add.getCtgCfgScoringIndcResultDTOS(),indicatorCode);
    }

    @Override
    public void deleteScoringIndc(String indicatorCode) {
        CtgCfgScoringIndc ctgCfgScoringIndc = ctgCfgScoringIndcRepository.findByIndicatorCode(indicatorCode);
        ctgCfgScoringIndcRepository.delete(ctgCfgScoringIndc);
        ctgCfgScoringIndcResultService.delete(indicatorCode);
    }

    @Override
    public CtgCfgScoringIndcAdd getOne(String indicatorCode) {
        CtgCfgScoringIndcAdd ctgCfgScoringIndcAdd = new CtgCfgScoringIndcAdd();
        CtgCfgScoringIndc ctgCfgScoringIndc = ctgCfgScoringIndcRepository.findByIndicatorCode(indicatorCode);
        CtgCfgScoringIndcDto ctgCfgScoringIndcDto = new CtgCfgScoringIndcDto();
        BeanUtils.copyProperties(ctgCfgScoringIndc,ctgCfgScoringIndcDto);
        ctgCfgScoringIndcDto.setDesc(ctgCfgScoringIndc.getDescription());
        List<CtgCfgScoringIndcResultDTO> dtos = ctgCfgScoringIndcResultService.getAllResult(indicatorCode);
        ctgCfgScoringIndcAdd.setCtgCfgScoringIndcDto(ctgCfgScoringIndcDto);
        ctgCfgScoringIndcAdd.setCtgCfgScoringIndcResultDTOS(dtos);
        return ctgCfgScoringIndcAdd;
    }

    @Override
    public Page<CtgCfgScoringIndcDto> getAllScoringIndcMapp(String groupCode, String keyword, Pageable pageable) {
        BigDecimal keywordNumber;
        try {
            keywordNumber = new BigDecimal(keyword);
        } catch (NumberFormatException ignored) {
            keywordNumber = null;
        }
        return ctgCfgScoringIndcRepository.getPageData(groupCode,keywordNumber,keyword, pageable);
    }

    @Override
    public boolean checkCode(String indicatorCode) {
        return ctgCfgScoringIndcRepository.existsByIndicatorCode(indicatorCode);
    }
}
