package ngvgroup.com.bpmn.service.impl;

import ngvgroup.com.bpmn.dto.CtgCfgReportParam.CtgCfgReportParamDTO;
import ngvgroup.com.bpmn.dto.CtgCfgReportParam.ReportMiningParamDTO;
import ngvgroup.com.bpmn.dto.CtgCfgReportParam.ReportParamDto;
import ngvgroup.com.bpmn.dto.CtgCfgReportParam.ResourceParamDTO;
import ngvgroup.com.bpmn.dto.CtgCfgReportParamCond.ReportParamCondDTO;
import ngvgroup.com.bpmn.exception.BusCommonErrorCode;
import ngvgroup.com.bpmn.mapper.CtgCfgReportParam.CtgCfgReportParamMapper;
import ngvgroup.com.bpmn.model.CtgCfgReport;
import ngvgroup.com.bpmn.model.CtgCfgReportParam;
import ngvgroup.com.bpmn.model.CtgCfgReportParamCond;
import ngvgroup.com.bpmn.repository.CtgCfgReportParamCondRepository;
import ngvgroup.com.bpmn.repository.CtgCfgReportParamRepository;
import ngvgroup.com.bpmn.service.CtgCfgReportParamService;
import ngvgroup.com.bpmn.utils.SQLValidatorUtils;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
import com.ngvgroup.bpm.core.security.JwtAuthConverter;
import com.ngvgroup.bpm.core.service.BaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CtgCfgReportParamServiceImpl extends BaseService implements CtgCfgReportParamService {

    private final CtgCfgReportParamRepository ctgCfgReportParamRepository;
    private final CtgCfgReportParamCondRepository ctgCfgReportParamCondRepository;
    private final CtgCfgReportParamMapper mapper;
    private final JwtAuthConverter jwtAuthConverter;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public CtgCfgReportParamDTO create(CtgCfgReportParamDTO reportParam) {
        boolean isExists = ctgCfgReportParamRepository.existsParameterCode(
                reportParam.getParameterCode(),
                reportParam.getReportCode());
        if (isExists) {
            throw new BusinessException(ErrorCode.CONFLICT, reportParam.getParameterCode());
        }
        CtgCfgReportParam ctgCfgReportParam = mapper.toEntity(reportParam);
        Timestamp current = Timestamp.valueOf(LocalDateTime.now());
        ctgCfgReportParam.setRecordStatus("approval");
        ctgCfgReportParam.setCreatedDate(current);
        ctgCfgReportParam.setCreatedBy(getCurrentUserName());
        ctgCfgReportParam.setApprovedDate(current);
        ctgCfgReportParam.setApprovedBy(getCurrentUserName());
        ctgCfgReportParam.setIsDelete(0);
        ctgCfgReportParam.setIsActive(1);
        return mapper.toDto(ctgCfgReportParamRepository.save(ctgCfgReportParam));
    }

    @Override
    public CtgCfgReportParamDTO update(CtgCfgReportParamDTO reportParam) {
        Optional<CtgCfgReportParam> reportParamOptional = ctgCfgReportParamRepository.findById(reportParam.getId());
        if (reportParamOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, reportParam.getParameterCode());
        }
        CtgCfgReportParam ctgCfgReportParam = mapper.toEntity(reportParam);
        ctgCfgReportParam.setId(reportParamOptional.get().getId());
        ctgCfgReportParam.setCreatedBy(reportParamOptional.get().getCreatedBy());
        ctgCfgReportParam.setCreatedDate(reportParamOptional.get().getCreatedDate());
        ctgCfgReportParam.setModifiedDate(Timestamp.valueOf(LocalDateTime.now()));
        ctgCfgReportParam.setModifiedBy(getCurrentUserName());
        ctgCfgReportParam.setApprovedDate(reportParamOptional.get().getApprovedDate());
        ctgCfgReportParam.setApprovedBy(reportParamOptional.get().getApprovedBy());
        ctgCfgReportParam.setModifiedBy(getCurrentUserName());
        ctgCfgReportParam.setIsDelete(reportParamOptional.get().getIsDelete());
        ctgCfgReportParam.setIsActive(reportParamOptional.get().getIsActive());
        ctgCfgReportParam.setRecordStatus(reportParamOptional.get().getRecordStatus());
        return mapper.toDto(ctgCfgReportParamRepository.save(ctgCfgReportParam));
    }

    @Override
    public List<CtgCfgReportParam> findAll() {
        return ctgCfgReportParamRepository.findAll();
    }

    @Override
    public CtgCfgReportParamDTO findByParameterCode(String parameterCode) {
        CtgCfgReportParam ctgCfgReportParam = ctgCfgReportParamRepository.findByParameterCode(parameterCode)
                .orElse(null);
        if (ctgCfgReportParam == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, parameterCode);
        }
        return mapper.toDto(ctgCfgReportParam);
    }

    @Override
    public Page<ReportParamDto> searchAllByReportCode(String reportCode, String keyword, Pageable pageable) {
        return ctgCfgReportParamRepository.searchAllByReportCode(reportCode, keyword, pageable);
    }

    @Override
    public List<ResourceParamDTO> execResourceSql(String parameterCode, String reportCode) {
        String resourceSql = ctgCfgReportParamRepository.findResourceSqlBySourceParameterCode(parameterCode,
                reportCode);

        if (resourceSql == null) {
            return new ArrayList<>();
        }

        SQLValidatorUtils sqlValidator = new SQLValidatorUtils();
        if (!sqlValidator.isSafeSelectQuery(resourceSql)) {
            throw new BusinessException(ErrorCode.UNPROCESSABLE_ENTITY, reportCode);
        }

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(resourceSql);
        List<ResourceParamDTO> resourceParamDTOS = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            Object name = row.get("name");
            Object code = row.get("code");

            if (name == null || code == null) {
                continue;
            }
            resourceParamDTOS.add(new ResourceParamDTO(code.toString(), name.toString()));
        }

        return resourceParamDTOS;
    }

    @Override
    public CtgCfgReportParamDTO findById(Long id) {
        CtgCfgReportParam ctgCfgReportParam = ctgCfgReportParamRepository.findById(id).orElse(null);
        if (ctgCfgReportParam == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, id);
        }
        return mapper.toDto(ctgCfgReportParam);
    }

    @Transactional
    @Override
    public List<ReportMiningParamDTO> getAllByReport(String reportCode) {
        // Lấy tất cả các CtgCfgReportParam với reportCode
        List<CtgCfgReportParam> ctgCfgReportParamDTOS = ctgCfgReportParamRepository.findAllByReportCode(reportCode);

        // Tạo Map để lưu tên parameter theo code
        Map<String, String> paramCodeToNameMap = ctgCfgReportParamDTOS.stream()
                .collect(Collectors.toMap(CtgCfgReportParam::getParameterCode, CtgCfgReportParam::getParameterName));

        return ctgCfgReportParamDTOS.stream().map(rp -> {
            // Lấy resource params
            List<ResourceParamDTO> resourceParams = execResourceSql(rp.getParameterCode(), reportCode);

            // Lấy các điều kiện cho parameter hiện tại
            List<CtgCfgReportParamCond> paramConds = ctgCfgReportParamCondRepository
                    .findAllByReportCodeAndSourceParamCode(reportCode, rp.getParameterCode());

            return new ReportMiningParamDTO(
                    rp.getId(),
                    rp.getReportCode(),
                    rp.getParameterCode(),
                    rp.getParameterName(),
                    rp.getParameterNameEn(),
                    rp.getParameterType(),
                    rp.getControlType(),
                    rp.getResourceSql(),
                    rp.getIsDisplay(),
                    rp.getSortNumber(),
                    rp.getDisplayFrame(),
                    paramConds.stream().map(cond -> {
                        // Tìm phần tử có code = cond.getSourceParamValue()
                        String sourceParamName = resourceParams.stream()
                                .filter(r -> r.getCode().equals(cond.getSourceParamValue()))
                                .map(ResourceParamDTO::getName)
                                .findFirst()
                                .orElse("");

                        String sourceParamCodeName = paramCodeToNameMap.getOrDefault(cond.getSourceParamCode(), "");
                        String targetParamCodeName = paramCodeToNameMap.getOrDefault(cond.getTargetParamCode(), "");

                        return new ReportParamCondDTO(
                                cond.getId(),
                                cond.getReportCode(),
                                cond.getSourceParamCode(),
                                sourceParamCodeName,
                                cond.getSourceParamValue(),
                                sourceParamName,
                                cond.getTargetParamCode(),
                                targetParamCodeName,
                                cond.getConditionType(),
                                cond.getExpression(),
                                cond.getSortNumber(),
                                cond.getDescription());
                    }).collect(Collectors.toList()));
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {

        Optional<CtgCfgReportParam> ctgCfgReportParam = ctgCfgReportParamRepository.findById(id);
        if (!ctgCfgReportParam.isPresent()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, id);
        }

        List<CtgCfgReportParamCond> ctgCfgReportParamCond=ctgCfgReportParamCondRepository.findCtgCfgReportParamCondBySourceParamCodeAndReportCode(
                ctgCfgReportParam.get().getParameterCode(),
                ctgCfgReportParam.get().getReportCode()
        );
        if (!ctgCfgReportParamCond.isEmpty()) {
            throw new BusinessException(BusCommonErrorCode.SOURCE_PARAM_CODE_ALREADY_EXISTS, id);
        }

        List<CtgCfgReportParamCond> ctgCfgReportParamCond1=ctgCfgReportParamCondRepository.findCtgCfgReportParamCondByTargetParamCodeAndReportCode(
                ctgCfgReportParam.get().getParameterCode(),
                ctgCfgReportParam.get().getReportCode()
        );
        if (!ctgCfgReportParamCond1.isEmpty()) {
            throw new BusinessException(BusCommonErrorCode.TARGET_PARAM_CODE_ALREADY_EXISTS, id);
        }


        ctgCfgReportParamRepository.deleteById(id);
    }

    @Override
    public List<ReportParamDto> getAllResourceParamName(String reportCode) {
        return ctgCfgReportParamRepository.getAllResourceParamName(reportCode);
    }

    @Override
    public List<ReportParamDto> getAllTargetParamNames(String reportCode) {
        return ctgCfgReportParamRepository.getAllTargetParamNames(reportCode);
    }

    @Override
    public boolean checkExist(String reportCode,String parameterCode){
        Optional<CtgCfgReportParam> ctgCfgReportParam=ctgCfgReportParamRepository.findCtgCfgReportParamByReportCodeAndParameterCode(reportCode,parameterCode);
        if(!ctgCfgReportParam.isPresent()){
            return false;
        }
        return true;
    }

}