package ngvgroup.com.bpmn.service.impl;

import ngvgroup.com.bpmn.dto.CtgCfgReportParam.ResourceParamDTO;
import ngvgroup.com.bpmn.dto.CtgCfgReportParamCond.CtgCfgReportParamCondDTO;
import ngvgroup.com.bpmn.dto.CtgCfgReportParamCond.ReportParamCondDTO;
import ngvgroup.com.bpmn.mapper.CtgCfgReportParamCond.CtgCfgReportParamCondMapper;
import ngvgroup.com.bpmn.model.CtgCfgReportParamCond;
import ngvgroup.com.bpmn.repository.CtgCfgReportParamCondRepository;
import ngvgroup.com.bpmn.service.CtgCfgReportParamCondService;
import ngvgroup.com.bpmn.service.CtgCfgReportParamService;
import com.ngvgroup.bpm.core.service.BaseService;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CtgCfgReportParamCondServiceImpl extends BaseService implements CtgCfgReportParamCondService {

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
                    .anyMatch(record -> !record.getId().equals(paramCondDTO.getId()));
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
        comCfgReport.setIsActive(1);
        return mapper.toDto(ctgCfgReportParamCondRepository.save(comCfgReport));
    }

    @Override
    public CtgCfgReportParamCondDTO update(CtgCfgReportParamCondDTO paramCondDTO) {
        validateParamCodes(paramCondDTO.getSourceParamCode(), paramCondDTO.getTargetParamCode());

        checkDuplicateCondition(paramCondDTO);

        Optional<CtgCfgReportParamCond> comCfgReportParamCondOp = ctgCfgReportParamCondRepository
                .findById(paramCondDTO.getId());
        CtgCfgReportParamCond ctgCfgReportParamCond = mapper.toEntity(paramCondDTO);
        ctgCfgReportParamCond.setId(comCfgReportParamCondOp.get().getId());
        ctgCfgReportParamCond.setCreatedBy(comCfgReportParamCondOp.get().getCreatedBy());
        ctgCfgReportParamCond.setCreatedDate(comCfgReportParamCondOp.get().getCreatedDate());
        ctgCfgReportParamCond.setModifiedDate(Timestamp.valueOf(LocalDateTime.now()));
        ctgCfgReportParamCond.setModifiedBy(getCurrentUserName());
        ctgCfgReportParamCond.setApprovedDate(comCfgReportParamCondOp.get().getApprovedDate());
        ctgCfgReportParamCond.setApprovedBy(comCfgReportParamCondOp.get().getApprovedBy());
        ctgCfgReportParamCond.setModifiedBy(getCurrentUserName());
        ctgCfgReportParamCond.setIsDelete(comCfgReportParamCondOp.get().getIsDelete());
        ctgCfgReportParamCond.setIsActive(comCfgReportParamCondOp.get().getIsActive());
        ctgCfgReportParamCond.setRecordStatus(comCfgReportParamCondOp.get().getRecordStatus());
        return mapper.toDto(ctgCfgReportParamCondRepository.save(ctgCfgReportParamCond));
    }

    private void validateParamCodes(String sourceParamCode, String targetParamCode) {
        if (sourceParamCode != null && targetParamCode != null &&
                sourceParamCode.equals(targetParamCode)) {
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
        Page<ReportParamCondDTO> reportParamCondDTOS = ctgCfgReportParamCondRepository.searchAll(reportCode, keyword,
                pageable);
        List<ReportParamCondDTO> modifiedList = reportParamCondDTOS
                .stream()
                .peek(condDTO -> {
                    String parameterCode = condDTO.getSourceParamCode();
                    String resourceParamValue = condDTO.getSourceParamValue();
                    if (parameterCode != null && !parameterCode.isEmpty()) {
                        List<ResourceParamDTO> resourceParamDTOS = ctgCfgReportParamService
                                .execResourceSql(parameterCode, reportCode);

                        ResourceParamDTO matched = resourceParamDTOS.stream()
                                .filter(rp -> Objects.equals(rp.getCode(), resourceParamValue))
                                .findFirst()
                                .orElse(null);

                        condDTO.setSourceParamValueName(matched != null ? matched.getName() : null);
                    }
                })

                .toList();

        Page<ReportParamCondDTO> finalPage = new PageImpl<>(modifiedList, pageable,
                reportParamCondDTOS.getTotalElements());

        return finalPage;
    }
    //
    // @Override
    // public List<ReportParamCondDTO> getAllByReportCode(String reportCode) {
    // List<ReportParamCondDTO> reportParamCondDTOS =
    // comCfgReportParamCondRepository.getAllByReportCode(reportCode);
    // List<ReportParamCondDTO> modifiedList = reportParamCondDTOS
    // .stream()
    // .peek(condDTO -> {
    //// String parameterCode = condDTO.getParameterCode();
    //// String sourceParam = condDTO.getSourceParam();
    //// if (parameterCode != null && !parameterCode.isEmpty()) {
    //// List<ResourceParamDTO> resourceParamDTOS =
    // ctgCfgReportParamService.execResourceSql(parameterCode);
    //// ResourceParamDTO matched = resourceParamDTOS.stream()
    //// .filter(rp -> sourceParam.equals(rp.getCode()))
    //// .findFirst()
    //// .orElse(null);
    //// condDTO.setSourceParamValue(matched != null ? matched.getName() : null);
    //// }
    // })
    // .toList();
    // return modifiedList;
    // }
}
