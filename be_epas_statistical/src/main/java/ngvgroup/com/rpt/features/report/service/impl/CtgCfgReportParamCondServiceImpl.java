package ngvgroup.com.rpt.features.report.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam.ResourceParamDTO;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparamcond.CtgCfgReportParamCondDTO;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparamcond.ReportParamCondDTO;
import ngvgroup.com.rpt.features.report.mapper.CtgCfgReportParamCondMapper;
import ngvgroup.com.rpt.features.report.model.CtgCfgReportParamCond;
import ngvgroup.com.rpt.features.report.repository.CtgCfgReportParamCondRepository;
import ngvgroup.com.rpt.features.report.service.CtgCfgReportParamCondService;
import ngvgroup.com.rpt.features.report.service.CtgCfgReportParamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CtgCfgReportParamCondServiceImpl extends BaseStoredProcedureService implements CtgCfgReportParamCondService {

    private final CtgCfgReportParamCondRepository ctgCfgReportParamCondRepository;
    private final CtgCfgReportParamCondMapper mapper;
    private final CtgCfgReportParamService ctgCfgReportParamService;

    private void checkDuplicateCondition(CtgCfgReportParamCondDTO paramCondDTO) {
        List<CtgCfgReportParamCond> existingRecords = ctgCfgReportParamCondRepository
                .existingRecords(
                        paramCondDTO.getReportCode(),
                        paramCondDTO.getSourceParamCode(),
                        paramCondDTO.getSourceParamValue(),
                        paramCondDTO.getTargetParamCode());
        if (!existingRecords.isEmpty()) {
            boolean hasOther = existingRecords.stream()
                    .anyMatch(r -> !r.getId().equals(paramCondDTO.getId()));
            if (hasOther) {
                throw new BusinessException(ErrorCode.BAD_REQUEST,
                        ": Điều kiện ràng buộc tham số này đã tồn tại");
            }
        }
    }

    @Override
    public CtgCfgReportParamCondDTO create(CtgCfgReportParamCondDTO paramCondDTO) {
        validateParamCodes(paramCondDTO.getSourceParamCode(), paramCondDTO.getTargetParamCode());

        checkDuplicateCondition(paramCondDTO);

        CtgCfgReportParamCond comCfgReport = mapper.toEntity(paramCondDTO);
        Timestamp current = Timestamp.valueOf(LocalDateTime.now());
        comCfgReport.setRecordStatus("approval");
        comCfgReport.setCreatedDate(current);
        comCfgReport.setCreatedBy(getCurrentUserName());
        comCfgReport.setApprovedDate(current);
        comCfgReport.setApprovedBy(getCurrentUserName());
        comCfgReport.setIsDelete(0);
        return mapper.toDto(ctgCfgReportParamCondRepository.save(comCfgReport));
    }

    @Override
    public CtgCfgReportParamCondDTO update(CtgCfgReportParamCondDTO paramCondDTO) {
        validateParamCodes(paramCondDTO.getSourceParamCode(), paramCondDTO.getTargetParamCode());

        checkDuplicateCondition(paramCondDTO);

        CtgCfgReportParamCond comCfgReportParamCondOp = ctgCfgReportParamCondRepository
                .findById(paramCondDTO.getId()).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        CtgCfgReportParamCond ctgCfgReportParamCond = mapper.toEntity(paramCondDTO);
        ctgCfgReportParamCond.setId(comCfgReportParamCondOp.getId());
        ctgCfgReportParamCond.setCreatedBy(comCfgReportParamCondOp.getCreatedBy());
        ctgCfgReportParamCond.setCreatedDate(comCfgReportParamCondOp.getCreatedDate());
        ctgCfgReportParamCond.setModifiedDate(Timestamp.valueOf(LocalDateTime.now()));
        ctgCfgReportParamCond.setModifiedBy(getCurrentUserName());
        ctgCfgReportParamCond.setApprovedDate(comCfgReportParamCondOp.getApprovedDate());
        ctgCfgReportParamCond.setApprovedBy(comCfgReportParamCondOp.getApprovedBy());
        ctgCfgReportParamCond.setModifiedBy(getCurrentUserName());
        ctgCfgReportParamCond.setIsDelete(comCfgReportParamCondOp.getIsDelete());
        ctgCfgReportParamCond.setIsActive(comCfgReportParamCondOp.getIsActive());
        ctgCfgReportParamCond.setRecordStatus(comCfgReportParamCondOp.getRecordStatus());
        return mapper.toDto(ctgCfgReportParamCondRepository.save(ctgCfgReportParamCond));
    }

    private void validateParamCodes(String sourceParamCode, String targetParamCode) {
        if (sourceParamCode != null && sourceParamCode.equals(targetParamCode)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST,
                    ": Mã tham số nguồn và mã tham số đích không được trùng nhau");
        }
    }

    @Override
    public List<CtgCfgReportParamCond> findAll() {
        return ctgCfgReportParamCondRepository.findAll();
    }

    @Override
    public CtgCfgReportParamCondDTO findById(Long id) {
        CtgCfgReportParamCond ctgCfgReportParamCond = ctgCfgReportParamCondRepository.findById(id).orElse(null);
        return mapper.toDto(ctgCfgReportParamCond);
    }

    @Override
    public void deleteById(Long id) {
        ctgCfgReportParamCondRepository.deleteById(id);
    }

    @Override
    public Page<ReportParamCondDTO> searchAll(String reportCode, String keyword, Pageable pageable) {

        Page<ReportParamCondDTO> pageData =
                ctgCfgReportParamCondRepository.searchAll(reportCode, keyword, pageable);

        List<ReportParamCondDTO> modifiedList = new ArrayList<>(pageData.getContent());

        modifiedList.forEach(condDTO -> {
            String parameterCode = condDTO.getSourceParamCode();
            String resourceParamValue = condDTO.getSourceParamValue();

            if (parameterCode != null && !parameterCode.isEmpty()) {
                List<ResourceParamDTO> resourceParamDTOS =
                        ctgCfgReportParamService.execResourceSql(parameterCode, reportCode);

                ResourceParamDTO matched = resourceParamDTOS.stream()
                        .filter(rp -> Objects.equals(rp.getCode(), resourceParamValue))
                        .findFirst()
                        .orElse(null);

                condDTO.setSourceParamValueName(
                        matched != null ? matched.getName() : null
                );
            }
        });

        return new PageImpl<>(modifiedList, pageable, pageData.getTotalElements());
    }
}
