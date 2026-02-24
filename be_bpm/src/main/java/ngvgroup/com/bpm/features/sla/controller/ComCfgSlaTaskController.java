package ngvgroup.com.bpm.features.sla.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaDto;
import ngvgroup.com.bpm.features.sla.dto.TaskDto;
import ngvgroup.com.bpm.features.sla.service.ComCfgSlaTaskService;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaTaskDtlDto;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@PreAuthorize("hasRole('admin_sla')")
public class ComCfgSlaTaskController {

    private final ComCfgSlaTaskService comCfgSlaTaskService;

    public ComCfgSlaTaskController(ComCfgSlaTaskService comCfgSlaTaskService) {
        this.comCfgSlaTaskService = comCfgSlaTaskService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> createTask(@RequestBody ComCfgSlaDto comCfgSlaDto){
        comCfgSlaTaskService.createTask(comCfgSlaDto);
        return ResponseData.createdEntity();
    }

    @GetMapping("/get-list")
    public ResponseEntity<ResponseData<List<TaskDto>>> getTaskList(@RequestParam("processId") String processId) {

        return ResponseData.okEntity(comCfgSlaTaskService.getTasks(processId));
    }

    @GetMapping("/get-info/{processDefineCode}/{orgCode}")
    public ResponseEntity<ResponseData<ComCfgSlaDto>> getTaskInfo(@PathVariable("processDefineCode") String processDefineCode, @PathVariable("orgCode") String orgCode) {
        return ResponseData.okEntity(comCfgSlaTaskService.getInfoTasks(processDefineCode, orgCode));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseData<Void>> getTaskInfo(@RequestBody ComCfgSlaDto comCfgSlaDto){
        comCfgSlaTaskService.updateTasks(comCfgSlaDto);
        return ResponseData.okEntity(null);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseData<Void>> deleteTasks(@RequestBody ComCfgSlaDto comCfgSlaDto){

        comCfgSlaTaskService.deleteTasks(comCfgSlaDto);
        return ResponseData.okEntity(null);
    }

    @PostMapping("/get-detail/{processDefineCode}/{orgCode}")
    public ResponseEntity<ResponseData<Page<ComCfgSlaTaskDtlDto>>> getDetailTasks(@PathVariable("processDefineCode") String processDefineCode, @PathVariable("orgCode") String orgCode, @ParameterObject Pageable pageable) {

        return ResponseData.okEntity(comCfgSlaTaskService.getDetailTasks(processDefineCode, orgCode, pageable));
    }

    @GetMapping("/get-sum-sla-duration")
    public ResponseEntity<ResponseData<Double>> getSlaMaxDurationAuto() {
        return ResponseData.okEntity(comCfgSlaTaskService.getSlaMaxDurationAuto());
    }

    @GetMapping("/get-sum-sla-warning-duration")
    public ResponseEntity<ResponseData<Double>> getSlaWarningDurationAuto() {
        return ResponseData.okEntity(comCfgSlaTaskService.getSlaWarningDurationAuto());
    }

    @GetMapping("/get-sum-sla-warning-percent")
    public ResponseEntity<ResponseData<Double>> getSlaWarningPercentAuto() {
        return ResponseData.okEntity(comCfgSlaTaskService.slaWarningPercentAuto());
    }

}
