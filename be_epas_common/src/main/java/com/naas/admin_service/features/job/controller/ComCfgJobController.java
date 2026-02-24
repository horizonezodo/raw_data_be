package com.naas.admin_service.features.job.controller;

import com.naas.admin_service.features.job.dto.CreateJobRequest;
import com.naas.admin_service.features.job.dto.UpdateJobRequest;
import com.naas.admin_service.features.job.model.ComCfgJob;
import com.naas.admin_service.features.job.service.ComCfgJobService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.naas.admin_service.features.job.dto.ComCfgJobDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("cfg-jobs")
@RequiredArgsConstructor
public class ComCfgJobController  {

    private final ComCfgJobService jobService;

    @GetMapping("/search")
    public ResponseEntity<ResponseData<Page<ComCfgJobDto>>> search(
            @RequestParam(required = false) String search,
            @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(jobService.search(search, pageable));
    }


    // 3) CREATE + START luôn
    @PostMapping
    public ResponseEntity<ResponseData<ComCfgJob>> create(@RequestBody CreateJobRequest req) {
        ComCfgJob dto = jobService.createAndStartJob(req);
        return ResponseData.okEntity(dto);
    }

    // 4) UPDATE cron + description
    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<String>> update(
            @PathVariable Long id,
            @RequestBody UpdateJobRequest req) {

        jobService.updateJobCron(id, req);
        return ResponseData.okEntity("Success");
    }

    // 5) START job (set active + schedule cron)
    @PostMapping("/{id}/start")
    public ResponseEntity<ResponseData<Void>> start(@PathVariable Long id) {
        jobService.startJob(id);
        return ResponseData.okEntity(null);
    }

    // 6) STOP job (cancel cron, set inactive)
    @PostMapping("/{id}/stop")
    public ResponseEntity<ResponseData<Void>> stop(@PathVariable Long id) {
        jobService.stopJob(id);
        return ResponseData.okEntity(null);
    }

    // 7) DELETE job (có option xoá luôn log)
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> delete(
            @PathVariable Long id,
            @RequestParam(name = "deleteLogs", defaultValue = "false") boolean deleteLogs) {

        jobService.deleteJob(id, deleteLogs);
        return ResponseData.okEntity(null);
    }
}
