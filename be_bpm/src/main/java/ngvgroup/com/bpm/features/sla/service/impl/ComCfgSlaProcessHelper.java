package ngvgroup.com.bpm.features.sla.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.core.contants.BpmErrorCode;
import ngvgroup.com.bpm.core.contants.VariableConstants;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaProcessDtlDto;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaProcessDto;
import ngvgroup.com.bpm.features.sla.model.ComCfgSlaProcess;
import ngvgroup.com.bpm.features.sla.model.ComCfgSlaProcessDtl;
import ngvgroup.com.bpm.features.sla.repository.ComCfgSlaProcessDtlRepository;
import ngvgroup.com.bpm.features.sla.repository.ComCfgSlaProcessRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ComCfgSlaProcessHelper {

    private final ComCfgSlaProcessRepository comCfgSlaProcessRepository;
    private final ComCfgSlaProcessDtlRepository comCfgSlaProcessDtlRepository;

    @Transactional
    public void createProcess(ComCfgSlaProcessDto comCfgSlaProcessDto) {
        checkSla(comCfgSlaProcessDto, comCfgSlaProcessRepository);
    }

    static void checkSla(ComCfgSlaProcessDto comCfgSlaProcessDto, ComCfgSlaProcessRepository comCfgSlaProcessRepository) {
        Optional<ComCfgSlaProcessDto> slaProcess = comCfgSlaProcessRepository.findInfComCfgSlaProcess(
                comCfgSlaProcessDto.getProcessTypeCode(),
                comCfgSlaProcessDto.getOrgCode()
        );
        if (slaProcess.isPresent()) {
            throw new BusinessException(BpmErrorCode.ALREADY_EXISTS);

        }
        ComCfgSlaProcess comCfgSlaProcess = new ComCfgSlaProcess(
                comCfgSlaProcessDto.getOrgCode(),
                comCfgSlaProcessDto.getProcessTypeCode(),
                comCfgSlaProcessDto.getProcessDefineCode(),
                comCfgSlaProcessDto.getSlaType(),
                comCfgSlaProcessDto.getUnit(),
                VariableConstants.RECORD_STATUS,
                VariableConstants.IS_ACTIVE
        );
        comCfgSlaProcessRepository.save(comCfgSlaProcess);
    }

    @Transactional
    public void createProcessDtl(ComCfgSlaProcessDtlDto comCfgSlaProcessDtlDto) {
        checkSlaDtl(comCfgSlaProcessDtlDto, comCfgSlaProcessDtlRepository);
    }

    static void checkSlaDtl(ComCfgSlaProcessDtlDto comCfgSlaProcessDtlDto, ComCfgSlaProcessDtlRepository comCfgSlaProcessDtlRepository) {
        Optional<ComCfgSlaProcessDtlDto> slaProcessDtl = comCfgSlaProcessDtlRepository.findInfoProcessDtl(
                comCfgSlaProcessDtlDto.getProcessDefineCode(),
                comCfgSlaProcessDtlDto.getOrgCode()
        );
        if (slaProcessDtl.isPresent()) {
            throw new BusinessException(BpmErrorCode.ALREADY_EXISTS);
        }


        ComCfgSlaProcessDtl comCfgSlaProcessDtl = new ComCfgSlaProcessDtl(
                comCfgSlaProcessDtlDto.getOrgCode(),
                comCfgSlaProcessDtlDto.getProcessDefineCode(),
                comCfgSlaProcessDtlDto.getEffectiveDate(),
                comCfgSlaProcessDtlDto.getSlaMaxDuration(),
                comCfgSlaProcessDtlDto.getSlaWarningType(),
                comCfgSlaProcessDtlDto.getSlaWarningPercent(),
                comCfgSlaProcessDtlDto.getSlaWarningDuration(),
                VariableConstants.RECORD_STATUS,
                VariableConstants.IS_ACTIVE
        );
        comCfgSlaProcessDtlRepository.save(comCfgSlaProcessDtl);
    }

}
