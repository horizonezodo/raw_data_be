package ngvgroup.com.bpm.features.sla.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.core.contants.BpmErrorCode;
import ngvgroup.com.bpm.core.contants.VariableConstants;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaDto;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaTaskDtlDto;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaTaskDto;
import ngvgroup.com.bpm.features.sla.dto.TaskDto;
import ngvgroup.com.bpm.features.sla.model.ComCfgSlaTask;
import ngvgroup.com.bpm.features.sla.model.ComCfgSlaTaskDtl;
import ngvgroup.com.bpm.features.sla.repository.ComCfgSlaTaskDtlRepository;
import ngvgroup.com.bpm.features.sla.repository.ComCfgSlaTaskRepository;
import ngvgroup.com.bpm.features.sla.service.ComCfgSlaTaskService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ComCfgSlaTaskServiceImpl implements ComCfgSlaTaskService {

    private final ComCfgSlaTaskRepository comCfgSlaTaskRepository;
    private final ComCfgSlaTaskDtlRepository comCfgSlaTaskDtlRepository;
    private final RepositoryService repositoryService;

    @Transactional
    @Override
    public void createTask(ComCfgSlaDto cfgSlaDto) {

        for (ComCfgSlaTaskDto comCfgSlaTaskDto : cfgSlaDto.getComCfgSlaTaskDto()) {

            Optional<ComCfgSlaTaskDto> cfgSlaTaskDto = comCfgSlaTaskRepository.findComCfgSlaTask(
                    comCfgSlaTaskDto.getTaskDefineCode(),
                    comCfgSlaTaskDto.getOrgCode(),
                    comCfgSlaTaskDto.getProcessDefineCode()

            );
            if (cfgSlaTaskDto.isPresent()) {
                throw new BusinessException(BpmErrorCode.ALREADY_EXISTS);
            }

            ComCfgSlaTask comCfgSlaTask = new ComCfgSlaTask(
                    comCfgSlaTaskDto.getOrgCode(),
                    comCfgSlaTaskDto.getTaskDefineCode(),
                    comCfgSlaTaskDto.getProcessDefineCode(),
                    comCfgSlaTaskDto.getPriorityLevel(),
                    comCfgSlaTaskDto.getUnit(),
                    VariableConstants.RECORD_STATUS,
                    VariableConstants.IS_ACTIVE);
            comCfgSlaTaskRepository.save(comCfgSlaTask);
        }

        for (ComCfgSlaTaskDtlDto comCfgSlaTaskDtlDto : cfgSlaDto.getComCfgSlaTaskDtlDto()) {

            Optional<ComCfgSlaTaskDtlDto> cfgSlaTaskDtlDto = comCfgSlaTaskDtlRepository.findComCfgSlaTaskDtl(
                    comCfgSlaTaskDtlDto.getTaskDefineCode(),
                    comCfgSlaTaskDtlDto.getOrgCode(),
                    comCfgSlaTaskDtlDto.getProcessDefineCode());
            if (cfgSlaTaskDtlDto.isPresent()) {
                throw new BusinessException(BpmErrorCode.ALREADY_EXISTS);
            }

            ComCfgSlaTaskDtl comCfgSlaTaskDtl = new ComCfgSlaTaskDtl(
                    comCfgSlaTaskDtlDto.getOrgCode(),
                    comCfgSlaTaskDtlDto.getTaskDefineCode(),
                    comCfgSlaTaskDtlDto.getProcessDefineCode(),
                    comCfgSlaTaskDtlDto.getEffectiveDate(),
                    comCfgSlaTaskDtlDto.getSlaMaxDuration(),
                    comCfgSlaTaskDtlDto.getSlaWarningDuration(),
                    comCfgSlaTaskDtlDto.getSlaWarningType(),
                    comCfgSlaTaskDtlDto.getSlaWarningPercent(), 
                    VariableConstants.RECORD_STATUS,
                    VariableConstants.IS_ACTIVE);
            comCfgSlaTaskDtlRepository.save(comCfgSlaTaskDtl);
        }

    }

    @Override
    public List<TaskDto> getTasks(String processId) {
        if ("".equals(processId))
            return Collections.emptyList();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processId)
                .latestVersion()
                .singleResult();
        if (processDefinition == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, processId);
        }

        String processDefinitionId = processDefinition.getId();
        BpmnModelInstance bpmnModelInstance = repositoryService.getBpmnModelInstance(processDefinitionId);
        return bpmnModelInstance.getModelElementsByType(UserTask.class)
                .stream()
                .map(task -> new TaskDto(task.getId(), task.getName()))
                .toList();

    }

    @Override
    public ComCfgSlaDto getInfoTasks(String processDefineCode, String orgCode) {

        ComCfgSlaDto cfgSlaDto = new ComCfgSlaDto();

        List<ComCfgSlaTaskDto> comCfgSlaTaskDtos = comCfgSlaTaskRepository.getInfoTask(processDefineCode, orgCode);

        List<ComCfgSlaTaskDtlDto> comCfgSlaTaskDtlDtos = comCfgSlaTaskDtlRepository.getInfoTaskDtl(processDefineCode,
                orgCode);

        cfgSlaDto.setComCfgSlaTaskDto(comCfgSlaTaskDtos);
        cfgSlaDto.setComCfgSlaTaskDtlDto(comCfgSlaTaskDtlDtos);

        return cfgSlaDto;
    }

    @Override
    public void updateTasks(ComCfgSlaDto cfgSlaDto) {

        List<ComCfgSlaTaskDto> comCfgSlaTaskDtos = comCfgSlaTaskRepository.getInfoTask(
                cfgSlaDto.getComCfgSlaProcessDto().getProcessTypeCode(),
                cfgSlaDto.getComCfgSlaProcessDto().getOrgCode());
        if (comCfgSlaTaskDtos.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Task");
        }

        List<ComCfgSlaTaskDtlDto> comCfgSlaTaskDtlDtos = comCfgSlaTaskDtlRepository.getInfoTaskDtl(
                cfgSlaDto.getComCfgSlaProcessDto().getProcessTypeCode(),
                cfgSlaDto.getComCfgSlaProcessDto().getOrgCode());
        if (comCfgSlaTaskDtlDtos.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Task");
        }

        for (ComCfgSlaTaskDto comCfgSlaTaskDto : cfgSlaDto.getComCfgSlaTaskDto()) {

            log.info(comCfgSlaTaskDto.toString());
            comCfgSlaTaskRepository.updateTask(
                    comCfgSlaTaskDto.getPriorityLevel(),
                    comCfgSlaTaskDto.getOrgCode(),
                    comCfgSlaTaskDto.getTaskDefineCode(),
                    comCfgSlaTaskDto.getUnit());
        }

        for (ComCfgSlaTaskDtlDto comCfgSlaTaskDtlDto : cfgSlaDto.getComCfgSlaTaskDtlDto()) {

            log.info(comCfgSlaTaskDtlDto.toString());
            comCfgSlaTaskDtlRepository.updateTaskDtl(
                    comCfgSlaTaskDtlDto.getSlaMaxDuration(),
                    comCfgSlaTaskDtlDto.getSlaWarningDuration(),
                    comCfgSlaTaskDtlDto.getSlaWarningPercent(),
                    comCfgSlaTaskDtlDto.getOrgCode(),
                    comCfgSlaTaskDtlDto.getTaskDefineCode(),
                    comCfgSlaTaskDtlDto.getSlaWarningType());
        }

    }

    @Override
    public void deleteTasks(ComCfgSlaDto comCfgSlaDto) {
        List<ComCfgSlaTaskDto> comCfgSlaTaskDtos = comCfgSlaTaskRepository.getInfoTask(
                comCfgSlaDto.getProcessTypeCode(), comCfgSlaDto.getOrgCode());
        if (comCfgSlaTaskDtos.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Task");
        }

        List<ComCfgSlaTaskDtlDto> comCfgSlaTaskDtlDtos = comCfgSlaTaskDtlRepository.getInfoTaskDtl(
                comCfgSlaDto.getProcessTypeCode(), comCfgSlaDto.getOrgCode());
        if (comCfgSlaTaskDtlDtos.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Task");
        }
        for (ComCfgSlaTaskDto comCfgSlaTaskDto : comCfgSlaTaskDtos) {
            // exception

            comCfgSlaTaskRepository.deleteComCfgSlaTaskByOrgCodeAndProcessDefineCode(
                    comCfgSlaTaskDto.getOrgCode(),
                    comCfgSlaTaskDto.getProcessDefineCode());
        }

        for (ComCfgSlaTaskDtlDto comCfgSlaTaskDtlDto : comCfgSlaTaskDtlDtos) {

            comCfgSlaTaskDtlRepository.deleteComCfgSlaTaskDtlByOrgCodeAndProcessDefineCode(
                    comCfgSlaTaskDtlDto.getOrgCode(),
                    comCfgSlaTaskDtlDto.getProcessDefineCode());

        }

    }

    @Override
    public Double slaWarningPercentAuto() {
        return comCfgSlaTaskDtlRepository.slaWarningPercentAuto();
    }

    @Override
    public Page<ComCfgSlaTaskDtlDto> getDetailTasks(String processDefineCode, String orgCode, Pageable pageable) {
        return comCfgSlaTaskDtlRepository.getDetailTask(processDefineCode, orgCode, pageable);

    }

    @Override
    public Double getSlaMaxDurationAuto() {
        return comCfgSlaTaskDtlRepository.getSlaMaxDurationAuto();
    }

    @Override
    public Double getSlaWarningDurationAuto() {
        return comCfgSlaTaskDtlRepository.getSlaWarningDurationAuto();
    }

}
