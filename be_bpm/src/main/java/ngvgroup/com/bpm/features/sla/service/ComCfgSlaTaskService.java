package ngvgroup.com.bpm.features.sla.service;

import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaDto;
import ngvgroup.com.bpm.features.sla.dto.TaskDto;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaTaskDtlDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ComCfgSlaTaskService {

    void createTask(ComCfgSlaDto comCfgSlaDto) ;

    List<TaskDto> getTasks(String processId);

    ComCfgSlaDto getInfoTasks(String processDefineCode, String orgCode);

    void updateTasks(ComCfgSlaDto comCfgSlaDto) ;

    void deleteTasks(ComCfgSlaDto comCfgSlaDto) ;

    Double slaWarningPercentAuto();

    Page<ComCfgSlaTaskDtlDto> getDetailTasks(String processDefineCode, String orgCode, Pageable pageable);

    Double getSlaMaxDurationAuto();

    Double getSlaWarningDurationAuto();

}

