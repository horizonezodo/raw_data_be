package ngvgroup.com.bpm.features.sla.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.core.contants.BpmErrorCode;
import ngvgroup.com.bpm.features.sla.dto.UpdateSlaProcessDtlCmd;
import ngvgroup.com.bpm.features.common.dto.CommonDto;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaProcessDto;
import ngvgroup.com.bpm.features.sla.repository.ComCfgSlaProcessRepository;
import ngvgroup.com.bpm.features.sla.service.ComCfgSlaProcessService;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaProcessDtlDto;
import ngvgroup.com.bpm.features.sla.repository.ComCfgSlaProcessDtlRepository;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaDto;
import ngvgroup.com.bpm.features.sla.service.ComCfgSlaTaskService;
import ngvgroup.com.bpm.features.common.repository.CtgComCommonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ComCfgSlaProcessServiceImpl implements ComCfgSlaProcessService {

    private final ComCfgSlaProcessRepository comCfgSlaProcessRepository;
    private final CtgComCommonRepository ctgComCommonRepository;
    private final ComCfgSlaProcessDtlRepository comCfgSlaProcessDtlRepository;
    private final ComCfgSlaTaskService comCfgSlaTaskService;
    private final ComCfgSlaProcessHelper comCfgSlaProcessHelper;
    private final ExportExcel exportExcel;


    @Override
    public Page<ComCfgSlaDto.ComCfgSlaView> getListSla(Pageable pageable) {
        return comCfgSlaProcessRepository.getListSla(pageable);
    }

    @Override
    public Page<ComCfgSlaDto.ComCfgSlaView> findSlaByKeyword(String keyword, Pageable pageable) {
        try {
            Page<ComCfgSlaDto.ComCfgSlaView> comCfgSlaDtos = comCfgSlaProcessRepository.findSlaByKeyword(keyword, pageable);

            log.info("lấy danh sách SLA thành công ");
            return comCfgSlaDtos;
        } catch (Exception e) {
            log.error("lỗi lấy danh sách SLA {}", e.getMessage(), e);
            throw new BusinessException(BpmErrorCode.UNABLE_GET_LIST_PROCESS, e);
        }
    }

    @Override
    public ResponseEntity<byte[]> exportToExcel(String keyword, String fileName) {
        try {
            List<ComCfgSlaDto.ComCfgSlaView> views =
                    comCfgSlaProcessRepository.exportToExcel(keyword);

            List<ComCfgSlaDto> data = views.stream()
                    .map(ComCfgSlaDto::fromView)
                    .toList();

            String safeFileName =
                    URLEncoder.encode(fileName, StandardCharsets.UTF_8);

            return exportExcel.exportExcel(data, safeFileName);

        } catch (Exception e) {
            throw new BusinessException(BpmErrorCode.UNABLE_GET_LIST_PROCESS, e);
        }
    }


    @Override
    public List<CommonDto> getUnit() {
        return ctgComCommonRepository.getUnit();
    }

    @Override
    public List<CommonDto> getPriorityLevel() {
        return ctgComCommonRepository.getPriorityLevel();
    }

    @Override
    @Transactional
    public void create(ComCfgSlaDto comCfgSlaDto) {
        comCfgSlaProcessHelper.createProcess(comCfgSlaDto.getComCfgSlaProcessDto());
        comCfgSlaProcessHelper.createProcessDtl(comCfgSlaDto.getComCfgSlaProcessDtlDto());
    }

    @Transactional
    @Override
    public void createProcess(ComCfgSlaProcessDto comCfgSlaProcessDto) {
        ComCfgSlaProcessHelper.checkSla(comCfgSlaProcessDto, comCfgSlaProcessRepository);
    }

    @Override
    @Transactional
    public void createProcessDtl(ComCfgSlaProcessDtlDto comCfgSlaProcessDtlDto) {
        ComCfgSlaProcessHelper.checkSlaDtl(comCfgSlaProcessDtlDto, comCfgSlaProcessDtlRepository);
    }


    @Override
    public void updateProcess(ComCfgSlaDto comCfgSlaDto) {

        Optional<ComCfgSlaProcessDto> slaProcess = comCfgSlaProcessRepository.findInfComCfgSlaProcess(
                comCfgSlaDto.getComCfgSlaProcessDto().getProcessTypeCode(),
                comCfgSlaDto.getComCfgSlaProcessDto().getOrgCode()
        );
        if (slaProcess.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        Optional<ComCfgSlaProcessDtlDto> slaProcessDtl = comCfgSlaProcessDtlRepository.findInfoProcessDtl(
                comCfgSlaDto.getComCfgSlaProcessDtlDto().getProcessDefineCode(),
                comCfgSlaDto.getComCfgSlaProcessDtlDto().getOrgCode()
        );
        if (slaProcessDtl.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }

        comCfgSlaProcessRepository.updateSlaProcess(
                comCfgSlaDto.getComCfgSlaProcessDto().getProcessTypeCode(),
                comCfgSlaDto.getComCfgSlaProcessDto().getOrgCode(),
                comCfgSlaDto.getComCfgSlaProcessDto().getIsActive(),
                comCfgSlaDto.getComCfgSlaProcessDto().getUnit(),
                comCfgSlaDto.getComCfgSlaProcessDto().getSlaType()
        );

        comCfgSlaProcessDtlRepository.updateSlaProcessDtl(
                UpdateSlaProcessDtlCmd.builder()
                        .orgCode(comCfgSlaDto.getComCfgSlaProcessDtlDto().getOrgCode())
                        .processDefineCode(comCfgSlaDto.getComCfgSlaProcessDtlDto().getProcessDefineCode())
                        .slaWarningType(comCfgSlaDto.getComCfgSlaProcessDtlDto().getSlaWarningType())
                        .slaMaxDuration(comCfgSlaDto.getComCfgSlaProcessDtlDto().getSlaMaxDuration())
                        .slaWarningPercent(comCfgSlaDto.getComCfgSlaProcessDtlDto().getSlaWarningPercent())
                        .slaWarningDuration(comCfgSlaDto.getComCfgSlaProcessDtlDto().getSlaWarningDuration())
                        .effectiveDate(comCfgSlaDto.getComCfgSlaProcessDtlDto().getEffectiveDate())
                        .isActive(comCfgSlaDto.getComCfgSlaProcessDtlDto().getIsActive())
                        .build()
        );

    }


    @Override
    public void deleteProcessSla(ComCfgSlaDto comCfgSlaDto) {

        Optional<ComCfgSlaProcessDto> comCfgSlaProcess = comCfgSlaProcessRepository.findInfComCfgSlaProcess(
                comCfgSlaDto.getProcessTypeCode(),
                comCfgSlaDto.getOrgCode()
        );
        if (comCfgSlaProcess.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Process");
        }

        Optional<ComCfgSlaProcessDtlDto> comCfgSlaProcessDtl = comCfgSlaProcessDtlRepository.findInfoProcessDtl(
                comCfgSlaDto.getProcessTypeCode(),
                comCfgSlaDto.getOrgCode()
        );
        if (comCfgSlaProcessDtl.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Process");
        }

        comCfgSlaProcessRepository.deleteComCfgSlaProcessByOrgCodeAndProcessTypeCode(
                comCfgSlaDto.getOrgCode(),
                comCfgSlaDto.getProcessTypeCode()

        );

        comCfgSlaProcessDtlRepository.deleteComCfgSlaProcessDtlByOrgCodeAndProcessDefineCode(
                comCfgSlaDto.getOrgCode(),
                comCfgSlaDto.getProcessTypeCode()
        );

    }


    @Override
    public ComCfgSlaDto getInfoProcess(String orgCode, String processTypeCode) {
        ComCfgSlaProcessDto comCfgSlaProcess = comCfgSlaProcessRepository.findInfComCfgSlaProcess(processTypeCode, orgCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin SLA process"));

        ComCfgSlaProcessDtlDto comCfgSlaProcessDtl = comCfgSlaProcessDtlRepository.findInfoProcessDtl(processTypeCode, orgCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin chi tiết SLA process"));

        return new ComCfgSlaDto(comCfgSlaProcess, comCfgSlaProcessDtl);
    }

    @Override
    public void updateSlaWarningPercent(String processDefineCode, String orgCode) {
        Double slaWarningPercent = comCfgSlaTaskService.slaWarningPercentAuto();
        comCfgSlaProcessDtlRepository.updateSlaWarningPercent(processDefineCode, orgCode, slaWarningPercent);
    }


}
