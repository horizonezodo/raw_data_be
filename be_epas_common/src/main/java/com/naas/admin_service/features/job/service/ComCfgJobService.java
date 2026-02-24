package com.naas.admin_service.features.job.service;

import com.naas.admin_service.features.job.dto.ComCfgJobDto;
import com.naas.admin_service.features.job.dto.CreateJobRequest;
import com.naas.admin_service.features.job.dto.UpdateJobRequest;
import com.naas.admin_service.features.job.model.ComCfgJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ComCfgJobService {
    ComCfgJob createAndStartJob(CreateJobRequest createJobRequest);
    void startJob(Long jobId);
    void stopJob(Long jobId);
    void updateJobCron(Long jobId, UpdateJobRequest req);
    void deleteJob(Long jobId, boolean deleteLogs);
    Page<ComCfgJobDto> search(String search, Pageable pageable);
}
